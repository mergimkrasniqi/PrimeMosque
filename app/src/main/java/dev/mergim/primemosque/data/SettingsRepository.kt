package dev.mergim.primemosque.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek

/** Prayers whose displayed time can be corrected by a per-prayer offset. */
val ADJUSTABLE_PRAYERS = listOf(
    PrayerKey.FAJR, PrayerKey.SUNRISE, PrayerKey.DHUHR,
    PrayerKey.ASR, PrayerKey.MAGHRIB, PrayerKey.ISHA,
)

enum class DisplayOrientation(val degrees: Int) {
    LANDSCAPE(0),
    PORTRAIT(90),
    PORTRAIT_REVERSED(270),
    LANDSCAPE_FLIPPED(180),
}

enum class AppLanguage { SQ, EN }

enum class AppTheme { DARK, BLACK, EMERALD, MIDNIGHT, BURGUNDY, LIGHT, GOLD, BLUE, GREEN }

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

data class Settings(
    val mosqueName: String = "Xhamia",
    val place: String = "Prizren",
    val city: String = "Prizren",
    val orientation: DisplayOrientation = DisplayOrientation.PORTRAIT,
    val language: AppLanguage = AppLanguage.SQ,
    val theme: AppTheme = AppTheme.DARK,
    val nightMode: NightMode = NightMode.AFTER_30,
    // Recurring lecture (e.g. "Zgjimi i Zemrave") pinned on the board on
    // the chosen day, held after the chosen prayer.
    val lectureTitle: String = "Ligjërata javore",
    val lectureDay: LectureDay = LectureDay.OFF,
    val lecturePrayer: PrayerKey = PrayerKey.MAGHRIB,
    // Per-prayer correction in minutes, applied on top of the takvim times.
    val prayerAdjustments: Map<PrayerKey, Int> = emptyMap(),
)

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    private object Keys {
        val MOSQUE_NAME = stringPreferencesKey("mosque_name")
        val PLACE = stringPreferencesKey("place")
        val CITY = stringPreferencesKey("city")
        val ORIENTATION = stringPreferencesKey("orientation")
        val LANGUAGE = stringPreferencesKey("language")
        val THEME = stringPreferencesKey("theme")
        val NIGHT_MODE = stringPreferencesKey("night_mode")
        val LECTURE_TITLE = stringPreferencesKey("lecture_title")
        val LECTURE_DAY = stringPreferencesKey("lecture_day")
        val LECTURE_PRAYER = stringPreferencesKey("lecture_prayer")

        fun adjustment(key: PrayerKey) = intPreferencesKey("adjust_${key.name.lowercase()}")
    }

    val settings: Flow<Settings> = context.dataStore.data.map { p ->
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
            theme = p[Keys.THEME]
                ?.let { runCatching { AppTheme.valueOf(it) }.getOrNull() }
                ?: defaults.theme,
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

    suspend fun setTheme(value: AppTheme) =
        context.dataStore.edit { it[Keys.THEME] = value.name }

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
    }
}
