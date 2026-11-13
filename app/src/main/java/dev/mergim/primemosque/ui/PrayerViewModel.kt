package dev.mergim.primemosque.ui

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.mergim.primemosque.data.ADHAN_PRAYERS
import dev.mergim.primemosque.data.AppLanguage
import dev.mergim.primemosque.data.BAJRAM_MINUTES_RANGE
import dev.mergim.primemosque.data.AppTheme
import dev.mergim.primemosque.data.City
import dev.mergim.primemosque.data.DEFAULT_ADHAN_SECONDS
import dev.mergim.primemosque.data.DisplayOrientation
import dev.mergim.primemosque.data.HadithRepository
import dev.mergim.primemosque.data.LectureDay
import dev.mergim.primemosque.data.NightMode
import dev.mergim.primemosque.data.NtpClock
import dev.mergim.primemosque.data.PrayerKey
import dev.mergim.primemosque.data.PrayerRepository
import dev.mergim.primemosque.data.PrayerSlot
import dev.mergim.primemosque.data.RemoteControl
import dev.mergim.primemosque.data.Settings
import dev.mergim.primemosque.data.SettingsRepository
import dev.mergim.primemosque.data.THEME_FAMILIES
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField

data class NextPrayer(val key: PrayerKey, val at: LocalDateTime)

/** The five daily prayers, as opposed to the sun-position slots. */
private val PRAYER_KEYS = ADHAN_PRAYERS.toSet()

/**
 * The three stages of the full-screen takeover around a prayer time: the
 * countdown through the last minutes before it, the adhan itself while the
 * muezzin calls, and the dua recited once he has finished.
 */
enum class AdhanPhase { COUNTDOWN, ADHAN, DUA }

/** The stage the board is in, and how long is left of it. */
data class AdhanSequence(
    val phase: AdhanPhase,
    val slot: PrayerSlot,
    val remaining: Duration,
)

/**
 * Contextual guidance shown on the board only while it applies: Friday
 * practices, the Duha prayer window, morning/evening dhikr, the end of the
 * Sabah time, reminders the evening before sunnah fasting days, and the
 * special days and seasons of the Hijri year.
 */
enum class NoticeKey {
    FRIDAY_KAHF,
    FRIDAY_SUNNAH,
    FRIDAY_DUA,
    FRIDAY_KHUTBAH,
    DUHA,
    MORNING_DHIKR,
    EVENING_DHIKR,
    FAST_MONDAY,
    FAST_THURSDAY,
    FAST_WHITE_DAYS,
    SABAH_ENDING,
    BAJRAM_EVE,
    DHUL_HIJJA_DAYS,
    ARAFAH_DAY,
    FAST_ARAFAH,
    FAST_TASUA,
    FAST_ASHURA,
    SHAWWAL_FAST,
    LAST_TEN_NIGHTS,
    CUSTOM,
}

/**
 * A notice to show. [arg] fills a number into its text (the day of a White
 * Day, minutes left), [event] and [time] name a Bajram and its namaz time.
 */
data class Notice(
    val key: NoticeKey,
    val arg: Int? = null,
    val custom: String? = null,
    val event: String? = null,
    val time: LocalTime? = null,
)

enum class BajramKind(val eventKey: String) {
    FITR("eid_al_fitr"),
    ADHA("eid_al_adha"),
}

/**
 * A day of Bajram: Fitër Bajrami lasts three days, Kurban Bajrami four.
 * [namazTime] is set on the first day, when the Bajram namaz is held.
 */
data class Bajram(val kind: BajramKind, val day: Int, val namazTime: LocalTime?)

/** The recurring lecture, pinned on its day until the prayer it follows. */
data class LectureInfo(val title: String, val prayer: PrayerKey, val time: LocalTime?)

data class HijriDate(val day: Int, val month: Int, val year: Int)

data class UpcomingEvent(val key: String, val date: LocalDate)

data class UiState(
    val now: LocalDateTime = LocalDateTime.now(),
    val slots: List<PrayerSlot> = emptyList(),
    val current: PrayerKey? = null,
    val next: NextPrayer? = null,
    val adhan: AdhanSequence? = null,
    val countdown: Duration = Duration.ZERO,
    val hijri: HijriDate? = null,
    val upcomingEvent: UpcomingEvent? = null,
    val notices: List<Notice> = emptyList(),
    val lecture: LectureInfo? = null,
    val khutbah: Boolean = false,
    val night: Boolean = false,
    // Daily wisdom break: the prayer table briefly gives way to rotating
    // Qur'an verses and hadiths (khutbah-style card).
    val quotesBreak: Boolean = false,
    // Which quote the break is showing, counted in elapsed quote slots rather
    // than in wall-clock ticks. Ticking the clock directly would step the
    // index by the whole cycle each time, and any step sharing a factor with
    // the collection size would leave part of the collection never shown.
    val quoteSlot: Int = 0,
    // Bundled quotes the break rotates through in the board's current
    // language (the Albanian board mixes in the Rijadus Salihin).
    val dailyQuotes: List<KhutbahQuote> = emptyList(),
    // Ramadan: active during the Hijri month of Ramadan (if enabled); the
    // board pins today's iftar and the relevant imsak (tomorrow's after
    // iftar has passed) and counts down to Iftar.
    val ramadan: Boolean = false,
    val iftarTime: LocalTime? = null,
    val imsakTime: LocalTime? = null,
    // Bajram days: the board pins the greeting, and on the first morning the
    // countdown leads to the Bajram namaz once Sabah has passed.
    val bajram: Bajram? = null,
    val countdownToBajram: Boolean = false,
    val settings: Settings = Settings(),
    val loaded: Boolean = false,
    // The clock reads earlier than a time the app has already lived through
    // and NTP has not synced: the TV clock is provably wrong (power cut).
    val clockSuspect: Boolean = false,
    // Theme the board should render with right now: the chosen theme, or —
    // with weekly rotation on — this week's family in the chosen variant.
    val theme: AppTheme = AppTheme.MUSHAF,
    // Pairing code for the web portal (null while remote control is off).
    val boardCode: String? = null,
    // Language the board should render right now: the primary, or — with a
    // secondary language set — alternating between the two in fixed blocks.
    val language: AppLanguage = AppLanguage.SQ,
)

class PrayerViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = PrayerRepository(app)
    private val settingsRepository = SettingsRepository(app)

    // A thousand narrations: parsed on first use, which happens on the
    // Default dispatcher where the state is built, never on the main thread.
    // Only those that fit the card are shown; the longest run to pages and
    // would be clipped mid-sentence, unreadable in a 30 s slot anyway.
    // Verses (the curated entries carrying the Arabic) take every other
    // turn, as they did before the collection arrived: mixed in by count
    // alone, a hundred verses among eight hundred narrations would barely
    // ever come round.
    private val albanianDailyQuotes by lazy {
        val rijad = runCatching { HadithRepository(app).rijadusSalihin() }
            .getOrDefault(emptyList())
            .filter { it.text.length <= MAX_QUOTE_CHARS }
            .map { KhutbahQuote(it.text, RIJAD_SOURCE) }
        val (verses, hadiths) = stringsFor(AppLanguage.SQ).dailyQuotes
            .partition { it.arabic != null }
        alternate(verses, interleave(hadiths, rijad))
    }

    // Kosovo timezone, independent of (possibly wrong) device settings.
    private val zone = ZoneId.of("Europe/Belgrade")

    // The upcoming Islamic day only changes with the date (or the Hijri
    // correction), so it is worked out once per day, not on every tick.
    private var eventCache: Triple<LocalDate, Int, UpcomingEvent?>? = null

    val cities: List<City> = repository.cities

    // TVs lose the clock on every power cut (no RTC battery), so the board
    // runs on NTP time whenever it can get it. Sync attempts are gated on
    // connectivity: with no network the loop stays suspended at no cost, and
    // NtpClock falls back to the TV clock. The moment any network appears
    // (Wi-Fi returning after a power cut, a phone hotspot), the time heals.
    private val connectivity = app.getSystemService(ConnectivityManager::class.java)
    private val networkUp = MutableStateFlow(false)
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) { networkUp.value = true }
        override fun onLost(network: Network) { networkUp.value = false }
    }

    init {
        // Web-portal remote control (no-op until Firebase is configured).
        RemoteControl.start(app, settingsRepository, viewModelScope)
        connectivity?.registerDefaultNetworkCallback(networkCallback)
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                networkUp.first { it } // suspends until a network is up
                delay(if (NtpClock.sync()) RESYNC_INTERVAL_MS else RETRY_INTERVAL_MS)
            }
        }
        // Remember the latest credible time so a clock that boots up in the
        // past can be recognised. Never lowered: while the clock is wrong
        // (behind), the last credible value must survive to keep the warning
        // up; it resumes advancing once the clock is corrected or overtakes.
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                // A failed write (full/failing flash) must never kill the
                // board; the next attempt comes in a few minutes anyway.
                runCatching {
                    val epoch = NtpClock.epochMs()
                    if (epoch > settingsRepository.lastSeenEpochMs.first()) {
                        settingsRepository.setLastSeenEpochMs(epoch)
                    }
                }
                delay(LAST_SEEN_INTERVAL_MS)
            }
        }
    }

    override fun onCleared() {
        connectivity?.unregisterNetworkCallback(networkCallback)
        super.onCleared()
    }

    private val ticker = flow {
        while (true) {
            emit(NtpClock.now(zone))
            delay(1_000L - (System.currentTimeMillis() % 1_000L))
        }
    }

    val uiState: StateFlow<UiState> =
        combine(
            ticker,
            settingsRepository.settings,
            settingsRepository.lastSeenEpochMs,
            settingsRepository.boardCode,
        ) { now, settings, lastSeen, boardCode ->
            // A setting the board cannot use (a bad value from the portal, or
            // one left by an older version) must not take the board down on
            // every tick: it falls back to the default settings, keeping the
            // mosque's name and layout, until the setting is corrected.
            runCatching { buildState(now, settings, lastSeen, boardCode) }
                .getOrElse {
                    buildState(
                        now,
                        Settings(
                            mosqueName = settings.mosqueName,
                            place = settings.place,
                            city = settings.city,
                            orientation = settings.orientation,
                            language = settings.language,
                            theme = settings.theme,
                            setupDone = settings.setupDone,
                        ),
                        lastSeen,
                        boardCode,
                    )
                }
        }
            .flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState())

    private fun adjusted(slots: List<PrayerSlot>, settings: Settings): List<PrayerSlot> =
        slots.map { slot ->
            val minutes = settings.prayerAdjustments[slot.key] ?: 0
            if (minutes == 0) slot else slot.copy(time = slot.time.plusMinutes(minutes.toLong()))
        }

    private fun buildState(
        now: LocalDateTime,
        settings: Settings,
        lastSeenEpochMs: Long,
        boardCode: String?,
    ): UiState {
        // Clock sanity: time cannot flow backwards. If the current reading is
        // clearly before a moment the app has already lived through, the TV
        // clock was reset (power cut) — warn until NTP or a manual fix.
        val clockSuspect = !NtpClock.synced &&
            NtpClock.epochMs() + CLOCK_SLACK_MS < lastSeenEpochMs

        val offset = repository.offsetFor(settings.city)
        val today = now.toLocalDate()
        val friday = today.dayOfWeek == DayOfWeek.FRIDAY
        // On Fridays a mosque may hold Jumu'ah at its own fixed time instead
        // of the Dhuhr time; the announcement, khutbah notice and countdown
        // all follow it because they derive from the slot list.
        val jumuah = settings.jumuahMinutes.takeIf { friday && it in 0 until 24 * 60 }
        val slots = adjusted(repository.slotsFor(today, offset), settings).map { slot ->
            if (jumuah != null && slot.key == PrayerKey.DHUHR) {
                slot.copy(time = LocalTime.of(jumuah / 60, jumuah % 60))
            } else {
                slot
            }
        }

        val next = slots
            .filter { it.key in PRAYER_KEYS }
            .map { NextPrayer(it.key, it.time.atDate(today)) }
            .firstOrNull { it.at.isAfter(now) }
            ?: adjusted(repository.slotsFor(today.plusDays(1), offset), settings)
                .firstOrNull { it.key == PrayerKey.FAJR }
                ?.let { NextPrayer(it.key, it.time.atDate(today.plusDays(1))) }

        // The prayer whose time most recently passed; before Sabahu it is
        // still Jacia (whose period runs past midnight).
        val current = slots
            .filter { it.key in PRAYER_KEYS }
            .lastOrNull { !it.time.atDate(today).isAfter(now) }
            ?.key
            ?: PrayerKey.ISHA

        val adhan = adhanSequenceFor(now, today, slots, settings, clockSuspect)

        // Overnight energy saver: the mosque is empty between Isha and Imsak,
        // so the board drops to the theme's dark variant and dims. The delay
        // after Isha keeps the normal board up while the congregation is
        // still praying.
        val night = settings.nightMode.minutesAfterIsha?.let { delayMinutes ->
            val nightStart = slots.firstOrNull { it.key == PrayerKey.ISHA }
                ?.time?.atDate(today)?.plusMinutes(delayMinutes)
            val nightEnd = slots.firstOrNull { it.key == PrayerKey.IMSAK }
                ?.time?.atDate(today)
            nightStart != null && nightEnd != null &&
                (!now.isBefore(nightStart) || now.isBefore(nightEnd))
        } ?: false

        // Hijri correction for moon-sighting differences (±days).
        val hijrah = HijrahDate.from(today.plusDays(settings.hijriOffset.toLong()))
        val hijri = HijriDate(
            day = hijrah.get(ChronoField.DAY_OF_MONTH),
            month = hijrah.get(ChronoField.MONTH_OF_YEAR),
            year = hijrah.get(ChronoField.YEAR),
        )

        val hijrahTomorrow = HijrahDate.from(today.plusDays(settings.hijriOffset + 1L))
        val hijriTomorrow = HijriDate(
            day = hijrahTomorrow.get(ChronoField.DAY_OF_MONTH),
            month = hijrahTomorrow.get(ChronoField.MONTH_OF_YEAR),
            year = hijrahTomorrow.get(ChronoField.YEAR),
        )

        // Bajram days come from the same corrected Hijri calendar; the namaz
        // is held on the first morning, a set time after sunrise.
        fun bajramNamaz(daySlots: List<PrayerSlot>): LocalTime? =
            daySlots.firstOrNull { it.key == PrayerKey.SUNRISE }?.time
                ?.plusMinutes(settings.bajramMinutesAfterSunrise.toLong())
        val bajram = bajramDay(hijri)?.let { (kind, day) ->
            Bajram(kind, day, if (day == 1) bajramNamaz(slots) else null)
        }
        // On the first morning, once Sabah is past, the countdown leads to the
        // Bajram namaz rather than straight on to Dreka.
        val bajramNamazAt = bajram?.namazTime?.atDate(today)?.takeIf { it.isAfter(now) }
        val countdownToBajram = bajramNamazAt != null && next != null && bajramNamazAt.isBefore(next.at)
        // The evening before, the board announces tomorrow's Bajram namaz.
        val bajramEve = bajramDay(hijriTomorrow)
            ?.takeIf { (_, day) -> day == 1 }
            ?.let { (kind, _) ->
                kind to bajramNamaz(adjusted(repository.slotsFor(today.plusDays(1), offset), settings))
            }

        // Ramadan follows the (offset-corrected) Hijri calendar by itself.
        val ramadan = settings.ramadanMode && hijri.month == HIJRI_RAMADAN
        val iftarTime = if (ramadan) {
            slots.firstOrNull { it.key == PrayerKey.MAGHRIB }?.time
        } else null
        // Until iftar the board shows today's imsak; afterwards tomorrow's,
        // which is the one the fasting congregation needs for suhoor.
        val imsakTime = when {
            !ramadan -> null
            iftarTime != null && !iftarTime.atDate(today).isAfter(now) ->
                adjusted(repository.slotsFor(today.plusDays(1), offset), settings)
                    .firstOrNull { it.key == PrayerKey.IMSAK }?.time
            else -> slots.firstOrNull { it.key == PrayerKey.IMSAK }?.time
        }

        val notices = buildNotices(now, today, slots, hijri, hijriTomorrow, bajramEve, settings)

        // Full-screen khutbah takeover: from Jumu'ah time (after the 1-minute
        // announcement) for the configured duration, the board is replaced by
        // the silence reminder and rotating Jumu'ah quotes.
        // It starts where the adhan sequence ends, so the configured
        // duration is the time the reminder is actually on screen.
        val khutbah = friday && slots.firstOrNull { it.key == PrayerKey.DHUHR }
            ?.time?.atDate(today)
            ?.plusSeconds(sequenceLength(PrayerKey.DHUHR, settings))
            ?.let { at ->
                !now.isBefore(at) && now.isBefore(at.plusMinutes(settings.khutbahMinutes.toLong()))
            } == true

        // Recurring lecture: pinned on the configured weekday, announcing it
        // follows the configured prayer, until that prayer's time arrives.
        // From then on the congregation is at the lecture itself, so the
        // reminder has done its job and the board gets its room back for the
        // rest of the day.
        val lectureTime = slots.firstOrNull { it.key == settings.lecturePrayer }?.time
        val lecture = settings.lectureDay.dayOfWeek
            ?.takeIf { it == today.dayOfWeek && settings.lectureTitle.isNotBlank() }
            ?.takeIf { lectureTime == null || now.toLocalTime().isBefore(lectureTime) }
            ?.let {
                LectureInfo(
                    title = settings.lectureTitle,
                    prayer = settings.lecturePrayer,
                    time = lectureTime,
                )
            }

        // The next major Islamic day, from the board's own Hijri calendar, so
        // it follows the same ±day correction as the Hijri date and Ramadan
        // mode, and keeps working in every year, not only the takvim's.
        val upcomingEvent = eventCache
            ?.takeIf { it.first == today && it.second == settings.hijriOffset }
            ?.third
            ?: upcomingIslamicEvent(today, settings.hijriOffset).also {
                eventCache = Triple(today, settings.hijriOffset, it)
            }

        // Daily wisdom breaks: on a fixed clock-driven cycle the prayer table
        // is replaced by rotating verses/hadiths, then comes back. Suppressed
        // whenever something more important owns the screen (announcement,
        // khutbah) and during the night saver.
        val quotesBreak = settings.showDailyQuotes && adhan == null && !khutbah && !night &&
            now.toLocalTime().toSecondOfDay() % QUOTES_CYCLE_SECONDS >= QUOTES_TABLE_SECONDS

        // Cycles are counted from the epoch rather than from midnight: the
        // collection outlasts a day's worth of slots, and restarting it each
        // midnight would replay the same stretch daily while the part that
        // falls in the night saver never showed at all.
        val secondOfDay = now.toLocalTime().toSecondOfDay()
        val quoteCycle = today.toEpochDay() * (SECONDS_PER_DAY / QUOTES_CYCLE_SECONDS) +
            secondOfDay / QUOTES_CYCLE_SECONDS
        val quoteSlot = (quoteCycle * QUOTE_SLOTS_PER_BREAK).toInt() +
            ((secondOfDay % QUOTES_CYCLE_SECONDS - QUOTES_TABLE_SECONDS) / QUOTE_SLOT_SECONDS)
                .coerceAtLeast(0)

        // Mixed congregations: alternate the whole board between the
        // primary and secondary language in fixed clock-driven blocks.
        val language = settings.secondaryLanguage
            ?.takeIf { (secondOfDay / LANGUAGE_BLOCK_SECONDS) % 2 == 1 }
            ?: settings.language

        return UiState(
            now = now,
            slots = slots,
            current = current,
            next = next,
            adhan = adhan,
            countdown = when {
                countdownToBajram && bajramNamazAt != null -> Duration.between(now, bajramNamazAt)
                next != null -> Duration.between(now, next.at)
                else -> Duration.ZERO
            },
            hijri = hijri,
            upcomingEvent = upcomingEvent,
            notices = notices,
            lecture = lecture,
            khutbah = khutbah,
            night = night,
            quotesBreak = quotesBreak,
            quoteSlot = quoteSlot,
            dailyQuotes = if (language == AppLanguage.SQ) albanianDailyQuotes
            else stringsFor(language).dailyQuotes,
            ramadan = ramadan,
            iftarTime = iftarTime,
            imsakTime = imsakTime,
            bajram = bajram,
            countdownToBajram = countdownToBajram,
            settings = settings,
            loaded = true,
            clockSuspect = clockSuspect,
            theme = themeFor(today, settings),
            boardCode = boardCode,
            language = language,
        )
    }

    /** How long the muezzin needs for this prayer's adhan, in seconds. */
    private fun adhanLength(key: PrayerKey, settings: Settings): Long =
        (settings.adhanSeconds[key] ?: DEFAULT_ADHAN_SECONDS[key] ?: 180).toLong()

    /** Seconds the full-screen sequence occupies *after* the prayer time. */
    private fun sequenceLength(key: PrayerKey, settings: Settings): Long =
        if (settings.adhanSequence) adhanLength(key, settings) + settings.adhanDuaSeconds
        else ANNOUNCE_SECONDS

    /**
     * The full-screen takeover around a prayer time: a countdown through the
     * last minutes before it, the adhan itself for as long as the muezzin
     * needs, then the short dua that follows. Null outside those windows,
     * when the board shows the prayer table as usual.
     *
     * Like every other takeover this is a pure function of the clock, so a
     * board that reboots mid-adhan comes back into the right stage.
     */
    private fun adhanSequenceFor(
        now: LocalDateTime,
        today: LocalDate,
        slots: List<PrayerSlot>,
        settings: Settings,
        clockSuspect: Boolean,
    ): AdhanSequence? {
        for (slot in slots) {
            if (slot.key !in PRAYER_KEYS) continue
            val at = slot.time.atDate(today)

            // Sequence off: the board keeps the plain one-minute
            // "Koha e Namazit të ..." announcement it has always shown.
            if (!settings.adhanSequence) {
                val end = at.plusSeconds(ANNOUNCE_SECONDS)
                if (!now.isBefore(at) && now.isBefore(end)) {
                    return AdhanSequence(AdhanPhase.ADHAN, slot, Duration.between(now, end))
                }
                continue
            }

            val adhanEnd = at.plusSeconds(adhanLength(slot.key, settings))
            val duaEnd = adhanEnd.plusSeconds(settings.adhanDuaSeconds.toLong())
            // A countdown is only as trustworthy as the clock behind it: while
            // the TV time is provably wrong, the board skips it rather than
            // tick down to an adhan that will not come. The announcement
            // itself still runs, as it always has.
            val countdownStart = at.minusMinutes(settings.preAdhanMinutes.toLong())
            if (settings.preAdhanMinutes > 0 && !clockSuspect &&
                !now.isBefore(countdownStart) && now.isBefore(at)
            ) {
                return AdhanSequence(AdhanPhase.COUNTDOWN, slot, Duration.between(now, at))
            }
            if (!now.isBefore(at) && now.isBefore(adhanEnd)) {
                return AdhanSequence(AdhanPhase.ADHAN, slot, Duration.between(now, adhanEnd))
            }
            if (!now.isBefore(adhanEnd) && now.isBefore(duaEnd)) {
                return AdhanSequence(AdhanPhase.DUA, slot, Duration.between(now, duaEnd))
            }
        }
        return null
    }

    /**
     * Weekly rotation: each Monday the board moves to the next theme family,
     * keeping the light/dark character of the theme the user chose. Off, the
     * chosen theme is used as-is.
     */
    private fun themeFor(today: LocalDate, settings: Settings): AppTheme {
        if (!settings.themeRotation) return settings.theme
        // Epoch day -3 was a Monday, so this index increments on Mondays.
        val week = (today.toEpochDay() + 3).floorDiv(7)
        val family = THEME_FAMILIES[week.mod(THEME_FAMILIES.size)]
        return if (settings.theme.isDark) family.darkVariant else family
    }

    private fun buildNotices(
        now: LocalDateTime,
        today: LocalDate,
        slots: List<PrayerSlot>,
        hijri: HijriDate,
        hijriTomorrow: HijriDate,
        bajramEve: Pair<BajramKind, LocalTime?>?,
        settings: Settings,
    ): List<Notice> = buildList {
        val time = now.toLocalTime()
        fun timeOf(key: PrayerKey) = slots.firstOrNull { it.key == key }?.time

        val fajr = timeOf(PrayerKey.FAJR)
        val sunrise = timeOf(PrayerKey.SUNRISE)
        val dhuhr = timeOf(PrayerKey.DHUHR)
        val asr = timeOf(PrayerKey.ASR)
        val maghrib = timeOf(PrayerKey.MAGHRIB)
        val friday = today.dayOfWeek == DayOfWeek.FRIDAY

        // Around the khutbah nothing else matters: the silence notice
        // replaces every other one.
        val khutbah = friday && dhuhr != null &&
            !time.isBefore(dhuhr.minusMinutes(20)) && time.isBefore(dhuhr.plusMinutes(45))
        if (khutbah) {
            add(Notice(NoticeKey.FRIDAY_KHUTBAH))
            return@buildList
        }

        // The last minutes before sunrise: once the sun is up, Sabah can no
        // longer be prayed in its time, so the warning has the card to itself.
        if (fajr != null && sunrise != null && !time.isBefore(fajr) && time.isBefore(sunrise)) {
            val secondsLeft = Duration.between(time, sunrise).seconds
            if (secondsLeft <= SABAH_WARNING_MINUTES * 60) {
                add(Notice(NoticeKey.SABAH_ENDING, arg = ((secondsLeft + 59) / 60).toInt()))
                return@buildList
            }
        }

        // Custom mosque announcements rotate along with the other notices
        // all day while they are set — until their optional expiry date
        // (inclusive), after which they disappear by themselves.
        fun activeAnnouncement(text: String, until: String): String? {
            if (text.isBlank()) return null
            val untilDate = until.takeIf { it.isNotBlank() }
                ?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
                ?: return text
            return text.takeIf { !untilDate.isBefore(today) }
        }
        activeAnnouncement(settings.announcement1, settings.announcement1Until)
            ?.let { add(Notice(NoticeKey.CUSTOM, custom = it)) }
        activeAnnouncement(settings.announcement2, settings.announcement2Until)
            ?.let { add(Notice(NoticeKey.CUSTOM, custom = it)) }

        val afterAsr = asr != null && !time.isBefore(asr)
        val afterMaghrib = maghrib != null && !time.isBefore(maghrib)

        // The evening before a Bajram, its namaz time.
        if (bajramEve != null && afterAsr) {
            add(Notice(NoticeKey.BAJRAM_EVE, event = bajramEve.first.eventKey, time = bajramEve.second))
        }

        if (friday && (maghrib == null || time.isBefore(maghrib))) {
            add(Notice(NoticeKey.FRIDAY_KAHF))
            add(Notice(NoticeKey.FRIDAY_DUA))
            // Preparation sunnahs only make sense before Jumu'ah itself.
            if (dhuhr != null && time.isBefore(dhuhr.minusMinutes(20))) {
                add(Notice(NoticeKey.FRIDAY_SUNNAH))
            }
        }

        if (fajr != null && sunrise != null &&
            !time.isBefore(fajr) && time.isBefore(sunrise)
        ) {
            add(Notice(NoticeKey.MORNING_DHIKR))
        }

        // Duha becomes valid once the sun has risen a spear's height
        // (~20 min after sunrise) and lasts until shortly before Dhuhr.
        if (sunrise != null && dhuhr != null &&
            !time.isBefore(sunrise.plusMinutes(20)) && time.isBefore(dhuhr.minusMinutes(15))
        ) {
            add(Notice(NoticeKey.DUHA))
        }

        if (afterAsr && !afterMaghrib) {
            add(Notice(NoticeKey.EVENING_DHIKR))
        }

        // The special days and seasons of the Hijri year.
        addAll(hijriNotices(hijri, hijriTomorrow, afterAsr, afterMaghrib))

        // Sunnah-fasting reminders for the evening before, from Asr onwards.
        // Pointless during Ramadan, when everyone is already fasting, and
        // never for a day on which fasting is not allowed (the Bajrams and
        // the days of Teshrik).
        val ramadan = settings.ramadanMode &&
            (hijri.month == HIJRI_RAMADAN || hijriTomorrow.month == HIJRI_RAMADAN)
        if (!ramadan && afterAsr && voluntaryFastAllowed(hijriTomorrow)) {
            when (today.dayOfWeek) {
                DayOfWeek.SUNDAY -> add(Notice(NoticeKey.FAST_MONDAY))
                DayOfWeek.WEDNESDAY -> add(Notice(NoticeKey.FAST_THURSDAY))
                else -> {}
            }
            // The White Days (13/14/15 of the lunar month) each get a
            // reminder the evening before.
            if (hijriTomorrow.day in 13..15) {
                add(Notice(NoticeKey.FAST_WHITE_DAYS, arg = hijriTomorrow.day))
            }
        }
    }

    fun setMosqueName(value: String) = viewModelScope.launch { settingsRepository.setMosqueName(value) }
    fun setPlace(value: String) = viewModelScope.launch { settingsRepository.setPlace(value) }
    fun setCity(value: String) = viewModelScope.launch { settingsRepository.setCity(value) }
    fun setOrientation(value: DisplayOrientation) = viewModelScope.launch { settingsRepository.setOrientation(value) }
    fun setLanguage(value: AppLanguage) = viewModelScope.launch { settingsRepository.setLanguage(value) }
    fun setSecondaryLanguage(value: AppLanguage?) =
        viewModelScope.launch { settingsRepository.setSecondaryLanguage(value) }
    fun setTheme(value: AppTheme) = viewModelScope.launch { settingsRepository.setTheme(value) }
    fun setThemeRotation(value: Boolean) =
        viewModelScope.launch { settingsRepository.setThemeRotation(value) }
    fun setNightMode(value: NightMode) = viewModelScope.launch { settingsRepository.setNightMode(value) }
    fun setLectureTitle(value: String) = viewModelScope.launch { settingsRepository.setLectureTitle(value) }
    fun setLectureDay(value: LectureDay) = viewModelScope.launch { settingsRepository.setLectureDay(value) }
    fun setLecturePrayer(value: PrayerKey) = viewModelScope.launch { settingsRepository.setLecturePrayer(value) }
    fun setPrayerAdjustment(key: PrayerKey, minutes: Int) =
        viewModelScope.launch { settingsRepository.setPrayerAdjustment(key, minutes.coerceIn(-60, 60)) }
    fun resetPrayerAdjustments() = viewModelScope.launch { settingsRepository.resetPrayerAdjustments() }
    fun completeSetup() = viewModelScope.launch { settingsRepository.setSetupDone() }
    fun setJumuahMinutes(value: Int) = viewModelScope.launch { settingsRepository.setJumuahMinutes(value) }
    fun setKhutbahMinutes(value: Int) =
        viewModelScope.launch { settingsRepository.setKhutbahMinutes(value.coerceIn(5, 45)) }
    fun setBajramMinutes(value: Int) =
        viewModelScope.launch {
            settingsRepository.setBajramMinutes(
                value.coerceIn(BAJRAM_MINUTES_RANGE.first, BAJRAM_MINUTES_RANGE.last)
            )
        }
    fun setAnnouncement1(value: String) = viewModelScope.launch { settingsRepository.setAnnouncement1(value) }
    fun setAnnouncement2(value: String) = viewModelScope.launch { settingsRepository.setAnnouncement2(value) }
    fun setHijriOffset(value: Int) =
        viewModelScope.launch { settingsRepository.setHijriOffset(value.coerceIn(-2, 2)) }
    fun setShowDailyQuotes(value: Boolean) =
        viewModelScope.launch { settingsRepository.setShowDailyQuotes(value) }
    fun setRamadanMode(value: Boolean) =
        viewModelScope.launch { settingsRepository.setRamadanMode(value) }
    fun setAdhanSequence(value: Boolean) =
        viewModelScope.launch { settingsRepository.setAdhanSequence(value) }
    fun setPreAdhanMinutes(value: Int) =
        viewModelScope.launch { settingsRepository.setPreAdhanMinutes(value.coerceIn(0, 10)) }
    fun setAdhanSeconds(key: PrayerKey, seconds: Int) =
        viewModelScope.launch {
            settingsRepository.setAdhanSeconds(key, seconds.coerceIn(30, 420))
        }
    fun setAdhanDuaSeconds(value: Int) =
        viewModelScope.launch { settingsRepository.setAdhanDuaSeconds(value.coerceIn(30, 60)) }

    private companion object {
        const val RESYNC_INTERVAL_MS = 60 * 60_000L
        // Network is up but the sync failed (DNS, captive portal, firewall).
        const val RETRY_INTERVAL_MS = 30_000L
        const val LAST_SEEN_INTERVAL_MS = 5 * 60_000L
        // With a secondary language set, the board switches language on
        // these block boundaries.
        const val LANGUAGE_BLOCK_SECONDS = 2 * 60
        // Month index of Ramadan in the Hijri calendar.
        const val HIJRI_RAMADAN = 9
        // Daily wisdom cycle: 30 s of the prayer table, then a single 30 s
        // quote, alternating all day. Nobody waits more than half a minute
        // for the times, and a passer-by who only glances on the way in still
        // has a fair chance of catching a verse.
        const val QUOTES_TABLE_SECONDS = 30
        const val QUOTES_BREAK_SECONDS = 30
        const val QUOTES_CYCLE_SECONDS = QUOTES_TABLE_SECONDS + QUOTES_BREAK_SECONDS
        // One quote fills a slot; a break is a whole number of them.
        const val QUOTE_SLOT_SECONDS = 30
        const val QUOTE_SLOTS_PER_BREAK = QUOTES_BREAK_SECONDS / QUOTE_SLOT_SECONDS
        // The last minutes before sunrise, when the board warns that the
        // Sabah time is running out.
        const val SABAH_WARNING_MINUTES = 20L
        const val SECONDS_PER_DAY = 24 * 60 * 60
        const val RIJAD_SOURCE = "Rijadu Salihin"
        // Longest narration that still fits the portrait card (about ten
        // lines) with a notice and the footer on screen.
        const val MAX_QUOTE_CHARS = 400
        // Tolerance before declaring the clock wrong, so small manual
        // corrections or minor drift never trigger the warning.
        const val CLOCK_SLACK_MS = 10 * 60_000L
        // Length of the plain announcement used when the adhan sequence is
        // switched off, in seconds.
        const val ANNOUNCE_SECONDS = 60L
    }
}

/**
 * Which day of which Bajram [hijri] is, if any: Fitër Bajrami is 1-3
 * Shevval, Kurban Bajrami 10-13 Dhul-Hixhe.
 */
internal fun bajramDay(hijri: HijriDate): Pair<BajramKind, Int>? = when {
    hijri.month == 10 && hijri.day in 1..3 -> BajramKind.FITR to hijri.day
    hijri.month == 12 && hijri.day in 10..13 -> BajramKind.ADHA to hijri.day - 9
    else -> null
}

/**
 * Whether a voluntary fast may be kept on [day]: never on either Bajram,
 * nor on the days of Teshrik (11-13 Dhul-Hixhe) that follow Kurban Bajrami.
 */
internal fun voluntaryFastAllowed(day: HijriDate): Boolean =
    !(day.month == 10 && day.day == 1) && !(day.month == 12 && day.day in 10..13)

/**
 * Reminders tied to the Hijri calendar: the first ten days of Dhul-Hixhe
 * and the day of Arafat, the evening-before reminders for the Arafat,
 * Tasua and Ashura fasts, the six days of Shevval, and the last ten nights
 * of Ramadan (from Maghrib on the 20th, when the 21st night begins).
 */
internal fun hijriNotices(
    today: HijriDate,
    tomorrow: HijriDate,
    afterAsr: Boolean,
    afterMaghrib: Boolean,
): List<Notice> = buildList {
    if (today.month == 12 && today.day == 9) add(Notice(NoticeKey.ARAFAH_DAY))
    if (today.month == 12 && today.day in 1..8) add(Notice(NoticeKey.DHUL_HIJJA_DAYS))
    if (afterAsr) {
        if (tomorrow.month == 12 && tomorrow.day == 9) add(Notice(NoticeKey.FAST_ARAFAH))
        if (tomorrow.month == 1 && tomorrow.day == 9) add(Notice(NoticeKey.FAST_TASUA))
        if (tomorrow.month == 1 && tomorrow.day == 10) add(Notice(NoticeKey.FAST_ASHURA))
    }
    // From the 2nd: fasting on the day of Bajram itself is not allowed.
    if (today.month == 10 && today.day >= 2) add(Notice(NoticeKey.SHAWWAL_FAST))
    if (today.month == 9 && (today.day >= 21 || (today.day == 20 && afterMaghrib))) {
        add(Notice(NoticeKey.LAST_TEN_NIGHTS))
    }
}

/** Major Islamic days by Hijri (month, day), named as in [Strings.eventNames]. */
private val HIJRI_EVENTS = mapOf(
    (1 to 1) to "islamic_new_year",
    (9 to 1) to "ramadan_start",
    // The night of the 27th begins at Maghrib on the 26th; the BIK takvim
    // lists Lejletul-Kadr under that evening's date.
    (9 to 26) to "laylat_al_qadr",
    (10 to 1) to "eid_al_fitr",
    (12 to 10) to "eid_al_adha",
)

/**
 * The first major Islamic day from [today] to [withinDays] days ahead, by the
 * Hijri calendar shifted by [hijriOffset] days for local moon sighting.
 */
internal fun upcomingIslamicEvent(
    today: LocalDate,
    hijriOffset: Int,
    withinDays: Int = 30,
): UpcomingEvent? = (0..withinDays).firstNotNullOfOrNull { ahead ->
    val date = today.plusDays(ahead.toLong())
    // HijrahDate covers 1300-1600 AH; past that the board shows no event.
    val hijri = runCatching { HijrahDate.from(date.plusDays(hijriOffset.toLong())) }
        .getOrNull() ?: return@firstNotNullOfOrNull null
    HIJRI_EVENTS[hijri.get(ChronoField.MONTH_OF_YEAR) to hijri.get(ChronoField.DAY_OF_MONTH)]
        ?.let { UpcomingEvent(it, date) }
}

/**
 * Pairs every narration with a verse, cycling through [verses] (the shorter
 * list) so the narrations still advance one by one and every verse keeps
 * coming round, a few times a day.
 */
private fun alternate(
    verses: List<KhutbahQuote>,
    narrations: List<KhutbahQuote>,
): List<KhutbahQuote> {
    if (verses.isEmpty() || narrations.isEmpty()) return verses + narrations
    return narrations.flatMapIndexed { i, narration ->
        listOf(verses[i % verses.size], narration)
    }
}

/**
 * Spreads [curated] evenly through [collection], so the hand-picked hadiths
 * keep coming round between the narrations instead of bunching at one end.
 */
private fun interleave(
    curated: List<KhutbahQuote>,
    collection: List<KhutbahQuote>,
): List<KhutbahQuote> {
    if (curated.isEmpty() || collection.isEmpty()) return curated + collection
    val every = maxOf(1, collection.size / curated.size)
    val picks = curated.iterator()
    return buildList {
        collection.forEachIndexed { i, quote ->
            if (i % every == 0 && picks.hasNext()) add(picks.next())
            add(quote)
        }
        picks.forEachRemaining(::add)
    }
}
