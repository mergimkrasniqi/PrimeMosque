package dev.mergim.primemosque.data

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException
import java.security.SecureRandom
import java.time.DayOfWeek

/** Prayers whose displayed time can be corrected by a per-prayer offset. */
val ADJUSTABLE_PRAYERS = listOf(
    PrayerKey.FAJR, PrayerKey.SUNRISE, PrayerKey.DHUHR,
    PrayerKey.ASR, PrayerKey.MAGHRIB, PrayerKey.ISHA,
)

/** Prayers announced full-screen: countdown, the adhan itself, then the dua. */
val ADHAN_PRAYERS = listOf(
    PrayerKey.FAJR, PrayerKey.DHUHR, PrayerKey.ASR, PrayerKey.MAGHRIB, PrayerKey.ISHA,
)

/**
 * Default adhan lengths in seconds for the sung makam style used in Kosovo.
 * Fajr carries the extra call ("as-salatu khayrun min an-nawm") and runs
 * longest; Maghrib is traditionally hurried, its window being the shortest.
 * Every muezzin keeps his own pace, so these are only a starting point --
 * the imam times his own and corrects them in the settings.
 */
val DEFAULT_ADHAN_SECONDS = mapOf(
    PrayerKey.FAJR to 210,
    PrayerKey.DHUHR to 180,
    PrayerKey.ASR to 180,
    PrayerKey.MAGHRIB to 120,
    PrayerKey.ISHA to 180,
)

enum class DisplayOrientation(val degrees: Int) {
    LANDSCAPE(0),
    PORTRAIT(90),
    PORTRAIT_REVERSED(270),
    LANDSCAPE_FLIPPED(180),
}

enum class AppLanguage { SQ, EN, TR, BS }

/** Four theme families, each in a light (day) and dark (night) variant. */
enum class AppTheme {
    MUSHAF, MUSHAF_DARK, ZAYTUN, ZAYTUN_DARK, NILA, NILA_DARK, HIBR, HIBR_DARK;

    val isDark: Boolean get() = name.endsWith("_DARK")

    /** The night-time twin of this theme (itself when already dark). */
    val darkVariant: AppTheme get() = if (isDark) this else valueOf("${name}_DARK")
}

/** The families the weekly rotation cycles through (light variants). */
val THEME_FAMILIES = listOf(AppTheme.MUSHAF, AppTheme.ZAYTUN, AppTheme.NILA, AppTheme.HIBR)

/**
 * Overnight energy saver: from Isha (plus the chosen delay, so the
 * congregation still sees the normal board while praying) until Imsak the
 * display switches to the black theme and dims the backlight.
 */
enum class NightMode(val minutesAfterIsha: Long?) {
    OFF(null),
    AT_ISHA(0),
    AFTER_15(15),
    AFTER_30(30),
    AFTER_45(45),
    AFTER_60(60),
}

/** Day of the recurring lecture; OFF hides the lecture banner entirely. */
enum class LectureDay(val dayOfWeek: DayOfWeek?) {
    OFF(null),
    MONDAY(DayOfWeek.MONDAY),
    TUESDAY(DayOfWeek.TUESDAY),
    WEDNESDAY(DayOfWeek.WEDNESDAY),
    THURSDAY(DayOfWeek.THURSDAY),
    FRIDAY(DayOfWeek.FRIDAY),
    SATURDAY(DayOfWeek.SATURDAY),
    SUNDAY(DayOfWeek.SUNDAY),
}

/**
 * A verse/hadith managed from the web portal. When the imam defines a list
 * there, it replaces the bundled texts on the board; an empty list means the
 * built-in collection is used. `**bold**` markup highlights key phrases.
 */
@Serializable
data class CustomQuote(
    val text: String,
    val source: String = "",
    val arabic: String? = null,
)

data class Settings(
    val mosqueName: String = "Xhamia",
    val place: String = "Prizren",
    val city: String = "Prizren",
    val orientation: DisplayOrientation = DisplayOrientation.PORTRAIT,
    val language: AppLanguage = AppLanguage.SQ,
    // Mixed congregations: when set, the board alternates between the
    // primary and this language in fixed blocks (null = single language).
    val secondaryLanguage: AppLanguage? = null,
    val theme: AppTheme = AppTheme.MUSHAF,
    // Rotate to the next theme family every week (keeping light/dark).
    val themeRotation: Boolean = false,
    val nightMode: NightMode = NightMode.AFTER_30,
    // Recurring lecture pinned on the board on
    // the chosen day, held after the chosen prayer.
    val lectureTitle: String = "Ligjërata javore",
    val lectureDay: LectureDay = LectureDay.OFF,
    val lecturePrayer: PrayerKey = PrayerKey.MAGHRIB,
    // Per-prayer correction in minutes, applied on top of the takvim times.
    val prayerAdjustments: Map<PrayerKey, Int> = emptyMap(),
    // First-run setup wizard has been completed.
    val setupDone: Boolean = false,
    // Fixed Jumu'ah time as minutes of day; -1 means "same as Dhuhr".
    val jumuahMinutes: Int = -1,
    // How long the full-screen khutbah takeover stays after Jumu'ah time.
    val khutbahMinutes: Int = 20,
    // Free-text mosque announcements, shown in the notice rotation while set.
    val announcement1: String = "",
    val announcement2: String = "",
    // Optional expiry (ISO date, inclusive): after this day the announcement
    // disappears from the board by itself. Empty = shown until cleared.
    val announcement1Until: String = "",
    val announcement2Until: String = "",
    // Hijri date correction in days (moon-sighting differences).
    val hijriOffset: Int = 0,
    // Daily wisdom breaks: every few minutes the prayer table gives way
    // briefly to rotating Qur'an verses and hadiths.
    val showDailyQuotes: Boolean = true,
    // Ramadan mode: during the Hijri month of Ramadan the board pins an
    // iftar/imsak banner and counts down to Iftar.
    val ramadanMode: Boolean = true,
    // Full-screen adhan sequence: a countdown through the last minutes
    // before the prayer time, the adhan itself while the muezzin calls, then
    // the short dua that follows it -- after which the table comes back.
    val adhanSequence: Boolean = true,
    // Length of the pre-adhan countdown in minutes (0 = straight to the adhan).
    val preAdhanMinutes: Int = 1,
    // Per-prayer adhan length in seconds; see [DEFAULT_ADHAN_SECONDS].
    val adhanSeconds: Map<PrayerKey, Int> = DEFAULT_ADHAN_SECONDS,
    // How long the adhan dua stays up afterwards. It is a short dua.
    val adhanDuaSeconds: Int = 45,
    // Portal-managed quote lists; empty = the bundled texts are used.
    val customDailyQuotes: List<CustomQuote> = emptyList(),
    val customKhutbahQuotes: List<CustomQuote> = emptyList(),
)

// A power cut can kill the TV mid-write and corrupt a preferences file;
// without a corruption handler DataStore then throws on every read and the
// app dies at startup until its data is cleared. Corrupt files are instead
// replaced with defaults, and the frequently-written runtime state lives in
// its own file so it can never take the mosque's configuration with it.
private val Context.dataStore by preferencesDataStore(
    name = "settings",
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
)

private val Context.runtimeDataStore by preferencesDataStore(
    name = "runtime",
    corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
)

/** Reads that survive a broken file: better defaults than a dead board. */
private fun Flow<Preferences>.orDefaults(): Flow<Preferences> =
    catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }

private val quotesJson = Json { ignoreUnknownKeys = true }

private fun String?.toQuotes(): List<CustomQuote> = this
    ?.let { runCatching { quotesJson.decodeFromString<List<CustomQuote>>(it) }.getOrNull() }
    ?: emptyList()

class SettingsRepository(private val context: Context) {

    private object Keys {
        val MOSQUE_NAME = stringPreferencesKey("mosque_name")
        val PLACE = stringPreferencesKey("place")
        val CITY = stringPreferencesKey("city")
        val ORIENTATION = stringPreferencesKey("orientation")
        val LANGUAGE = stringPreferencesKey("language")
        val SECONDARY_LANGUAGE = stringPreferencesKey("secondary_language")
        val THEME = stringPreferencesKey("theme")
        val THEME_ROTATION = booleanPreferencesKey("theme_rotation")
        val NIGHT_MODE = stringPreferencesKey("night_mode")
        val LECTURE_TITLE = stringPreferencesKey("lecture_title")
        val LECTURE_DAY = stringPreferencesKey("lecture_day")
        val LECTURE_PRAYER = stringPreferencesKey("lecture_prayer")

        val SETUP_DONE = booleanPreferencesKey("setup_done")
        val JUMUAH_MINUTES = intPreferencesKey("jumuah_minutes")
        val KHUTBAH_MINUTES = intPreferencesKey("khutbah_minutes")
        val ANNOUNCEMENT_1 = stringPreferencesKey("announcement_1")
        val ANNOUNCEMENT_2 = stringPreferencesKey("announcement_2")
        val ANNOUNCEMENT_1_UNTIL = stringPreferencesKey("announcement_1_until")
        val ANNOUNCEMENT_2_UNTIL = stringPreferencesKey("announcement_2_until")
        val RAMADAN_MODE = booleanPreferencesKey("ramadan_mode")
        val HIJRI_OFFSET = intPreferencesKey("hijri_offset")
        val DAILY_QUOTES = booleanPreferencesKey("daily_quotes")
        val CUSTOM_DAILY_QUOTES = stringPreferencesKey("custom_daily_quotes")
        val CUSTOM_KHUTBAH_QUOTES = stringPreferencesKey("custom_khutbah_quotes")
        val LAST_SEEN_EPOCH_MS = longPreferencesKey("last_seen_epoch_ms")
        val BOARD_CODE = stringPreferencesKey("board_code")

        val ADHAN_SEQUENCE = booleanPreferencesKey("adhan_sequence")
        val PRE_ADHAN_MINUTES = intPreferencesKey("pre_adhan_minutes")
        val ADHAN_DUA_SECONDS = intPreferencesKey("adhan_dua_seconds")

        fun adjustment(key: PrayerKey) = intPreferencesKey("adjust_${key.name.lowercase()}")

        fun adhanSeconds(key: PrayerKey) = intPreferencesKey("adhan_${key.name.lowercase()}")
    }

    val settings: Flow<Settings> = context.dataStore.data.orDefaults().map { p ->
        val defaults = Settings()
        Settings(
            mosqueName = p[Keys.MOSQUE_NAME] ?: defaults.mosqueName,
            place = p[Keys.PLACE] ?: defaults.place,
            city = p[Keys.CITY] ?: defaults.city,
            orientation = p[Keys.ORIENTATION]
                ?.let { runCatching { DisplayOrientation.valueOf(it) }.getOrNull() }
                ?: defaults.orientation,
            language = p[Keys.LANGUAGE]
                ?.let { runCatching { AppLanguage.valueOf(it) }.getOrNull() }
                ?: defaults.language,
            // "OFF" (and anything unknown) means no secondary language.
            secondaryLanguage = p[Keys.SECONDARY_LANGUAGE]
                ?.let { runCatching { AppLanguage.valueOf(it) }.getOrNull() },
            // Themes removed in an update fall back to the default.
            theme = p[Keys.THEME]
                ?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() }
                ?: defaults.theme,
            themeRotation = p[Keys.THEME_ROTATION] ?: defaults.themeRotation,
            nightMode = p[Keys.NIGHT_MODE]
                ?.let { runCatching { NightMode.valueOf(it) }.getOrNull() }
                ?: defaults.nightMode,
            lectureTitle = p[Keys.LECTURE_TITLE] ?: defaults.lectureTitle,
            lectureDay = p[Keys.LECTURE_DAY]
                ?.let { runCatching { LectureDay.valueOf(it) }.getOrNull() }
                ?: defaults.lectureDay,
            lecturePrayer = p[Keys.LECTURE_PRAYER]
                ?.let { runCatching { PrayerKey.valueOf(it) }.getOrNull() }
                ?: defaults.lecturePrayer,
            prayerAdjustments = ADJUSTABLE_PRAYERS.associateWith { key ->
                p[Keys.adjustment(key)] ?: 0
            },
            setupDone = p[Keys.SETUP_DONE] ?: defaults.setupDone,
            jumuahMinutes = p[Keys.JUMUAH_MINUTES] ?: defaults.jumuahMinutes,
            khutbahMinutes = p[Keys.KHUTBAH_MINUTES] ?: defaults.khutbahMinutes,
            announcement1 = p[Keys.ANNOUNCEMENT_1] ?: defaults.announcement1,
            announcement2 = p[Keys.ANNOUNCEMENT_2] ?: defaults.announcement2,
            announcement1Until = p[Keys.ANNOUNCEMENT_1_UNTIL] ?: defaults.announcement1Until,
            announcement2Until = p[Keys.ANNOUNCEMENT_2_UNTIL] ?: defaults.announcement2Until,
            ramadanMode = p[Keys.RAMADAN_MODE] ?: defaults.ramadanMode,
            hijriOffset = p[Keys.HIJRI_OFFSET] ?: defaults.hijriOffset,
            showDailyQuotes = p[Keys.DAILY_QUOTES] ?: defaults.showDailyQuotes,
            adhanSequence = p[Keys.ADHAN_SEQUENCE] ?: defaults.adhanSequence,
            preAdhanMinutes = p[Keys.PRE_ADHAN_MINUTES] ?: defaults.preAdhanMinutes,
            adhanSeconds = ADHAN_PRAYERS.associateWith { key ->
                p[Keys.adhanSeconds(key)] ?: DEFAULT_ADHAN_SECONDS[key] ?: 180
            },
            adhanDuaSeconds = p[Keys.ADHAN_DUA_SECONDS] ?: defaults.adhanDuaSeconds,
            customDailyQuotes = p[Keys.CUSTOM_DAILY_QUOTES].toQuotes(),
            customKhutbahQuotes = p[Keys.CUSTOM_KHUTBAH_QUOTES].toQuotes(),
        )
    }

    suspend fun setMosqueName(value: String) =
        context.dataStore.edit { it[Keys.MOSQUE_NAME] = value }

    suspend fun setPlace(value: String) =
        context.dataStore.edit { it[Keys.PLACE] = value }

    suspend fun setCity(value: String) =
        context.dataStore.edit { it[Keys.CITY] = value }

    suspend fun setOrientation(value: DisplayOrientation) =
        context.dataStore.edit { it[Keys.ORIENTATION] = value.name }

    suspend fun setLanguage(value: AppLanguage) =
        context.dataStore.edit { it[Keys.LANGUAGE] = value.name }

    suspend fun setSecondaryLanguage(value: AppLanguage?) =
        context.dataStore.edit { it[Keys.SECONDARY_LANGUAGE] = value?.name ?: "OFF" }

    suspend fun setTheme(value: AppTheme) =
        context.dataStore.edit { it[Keys.THEME] = value.name }

    suspend fun setThemeRotation(value: Boolean) =
        context.dataStore.edit { it[Keys.THEME_ROTATION] = value }

    suspend fun setNightMode(value: NightMode) =
        context.dataStore.edit { it[Keys.NIGHT_MODE] = value.name }

    suspend fun setLectureTitle(value: String) =
        context.dataStore.edit { it[Keys.LECTURE_TITLE] = value }

    suspend fun setLectureDay(value: LectureDay) =
        context.dataStore.edit { it[Keys.LECTURE_DAY] = value.name }

    suspend fun setLecturePrayer(value: PrayerKey) =
        context.dataStore.edit { it[Keys.LECTURE_PRAYER] = value.name }

    suspend fun setPrayerAdjustment(key: PrayerKey, minutes: Int) =
        context.dataStore.edit { it[Keys.adjustment(key)] = minutes }

    suspend fun resetPrayerAdjustments() = context.dataStore.edit { p ->
        ADJUSTABLE_PRAYERS.forEach { p.remove(Keys.adjustment(it)) }
        p.remove(Keys.HIJRI_OFFSET)
    }

    suspend fun setSetupDone() =
        context.dataStore.edit { it[Keys.SETUP_DONE] = true }

    suspend fun setJumuahMinutes(value: Int) =
        context.dataStore.edit { it[Keys.JUMUAH_MINUTES] = value }

    suspend fun setKhutbahMinutes(value: Int) =
        context.dataStore.edit { it[Keys.KHUTBAH_MINUTES] = value }

    suspend fun setAnnouncement1(value: String) =
        context.dataStore.edit { it[Keys.ANNOUNCEMENT_1] = value }

    suspend fun setAnnouncement2(value: String) =
        context.dataStore.edit { it[Keys.ANNOUNCEMENT_2] = value }

    suspend fun setAnnouncement1Until(value: String) =
        context.dataStore.edit { it[Keys.ANNOUNCEMENT_1_UNTIL] = value }

    suspend fun setAnnouncement2Until(value: String) =
        context.dataStore.edit { it[Keys.ANNOUNCEMENT_2_UNTIL] = value }

    suspend fun setRamadanMode(value: Boolean) =
        context.dataStore.edit { it[Keys.RAMADAN_MODE] = value }

    suspend fun setHijriOffset(value: Int) =
        context.dataStore.edit { it[Keys.HIJRI_OFFSET] = value }

    suspend fun setShowDailyQuotes(value: Boolean) =
        context.dataStore.edit { it[Keys.DAILY_QUOTES] = value }

    suspend fun setAdhanSequence(value: Boolean) =
        context.dataStore.edit { it[Keys.ADHAN_SEQUENCE] = value }

    suspend fun setPreAdhanMinutes(value: Int) =
        context.dataStore.edit { it[Keys.PRE_ADHAN_MINUTES] = value }

    suspend fun setAdhanSeconds(key: PrayerKey, seconds: Int) =
        context.dataStore.edit { it[Keys.adhanSeconds(key)] = seconds }

    suspend fun setAdhanDuaSeconds(value: Int) =
        context.dataStore.edit { it[Keys.ADHAN_DUA_SECONDS] = value }

    suspend fun setCustomDailyQuotes(value: List<CustomQuote>) =
        context.dataStore.edit { it[Keys.CUSTOM_DAILY_QUOTES] = quotesJson.encodeToString(value) }

    suspend fun setCustomKhutbahQuotes(value: List<CustomQuote>) =
        context.dataStore.edit { it[Keys.CUSTOM_KHUTBAH_QUOTES] = quotesJson.encodeToString(value) }

    // Pairing code for the web portal: the board listens to the Firestore
    // document named by this code, and the imam enters it in the portal.
    // Shown in the settings footer; generated once and kept forever.
    val boardCode: Flow<String?> =
        context.dataStore.data.orDefaults().map { it[Keys.BOARD_CODE] }

    suspend fun boardCodeOrCreate(): String {
        boardCode.first()?.let { return it }
        // No easily-confused characters (0/O, 1/I): the imam reads this off
        // the TV screen and types it into the portal.
        val alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        val random = SecureRandom()
        val code = buildString(8) {
            repeat(8) { append(alphabet[random.nextInt(alphabet.length)]) }
        }
        context.dataStore.edit { it[Keys.BOARD_CODE] = code }
        return code
    }

    // Most recent credible wall-clock time the app has seen, persisted so
    // that after a power cut a clock that boots up *behind* it can be
    // recognised as wrong (TVs have no RTC battery). Written every few
    // minutes, hence kept in the separate runtime file.
    val lastSeenEpochMs: Flow<Long> =
        context.runtimeDataStore.data.orDefaults().map { it[Keys.LAST_SEEN_EPOCH_MS] ?: 0L }

    suspend fun setLastSeenEpochMs(value: Long) =
        context.runtimeDataStore.edit { it[Keys.LAST_SEEN_EPOCH_MS] = value }
}
