package dev.mergim.primemosque.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.mergim.primemosque.data.AppLanguage
import dev.mergim.primemosque.data.AppTheme
import dev.mergim.primemosque.data.City
import dev.mergim.primemosque.data.DisplayOrientation
import dev.mergim.primemosque.data.LectureDay
import dev.mergim.primemosque.data.NightMode
import dev.mergim.primemosque.data.PrayerKey
import dev.mergim.primemosque.data.PrayerRepository
import dev.mergim.primemosque.data.PrayerSlot
import dev.mergim.primemosque.data.Settings
import dev.mergim.primemosque.data.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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

/**
 * Contextual guidance shown on the board only while it applies: Friday
 * practices, the Duha prayer window, morning/evening dhikr, and reminders
 * the evening before sunnah fasting days.
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
}

data class Notice(val key: NoticeKey, val arg: Int? = null)

/** The recurring lecture, pinned on the board for the whole configured day. */
data class LectureInfo(val title: String, val prayer: PrayerKey, val time: LocalTime?)

data class HijriDate(val day: Int, val month: Int, val year: Int)

data class UpcomingEvent(val key: String, val date: LocalDate)

data class UiState(
    val now: LocalDateTime = LocalDateTime.now(),
    val slots: List<PrayerSlot> = emptyList(),
    val current: PrayerKey? = null,
    val next: NextPrayer? = null,
    val announce: PrayerSlot? = null,
    val countdown: Duration = Duration.ZERO,
    val hijri: HijriDate? = null,
    val upcomingEvent: UpcomingEvent? = null,
    val notices: List<Notice> = emptyList(),
    val lecture: LectureInfo? = null,
    val night: Boolean = false,
    val settings: Settings = Settings(),
    val loaded: Boolean = false,
)

class PrayerViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = PrayerRepository(app)
    private val settingsRepository = SettingsRepository(app)

    // Kosovo timezone, independent of (possibly wrong) device settings.
    private val zone = ZoneId.of("Europe/Belgrade")

    // Minor events that should not appear on the board.
    private val hiddenEvents = setOf(
        "laylat_al_miraj", "laylat_al_baraat", "ashura", "mawlid",
    )

    val cities: List<City> = repository.cities

    private val ticker = flow {
        while (true) {
            emit(LocalDateTime.now(zone))
            delay(1_000L - (System.currentTimeMillis() % 1_000L))
        }
    }

    val uiState: StateFlow<UiState> =
        combine(ticker, settingsRepository.settings) { now, settings ->
            buildState(now, settings)
        }
            .flowOn(Dispatchers.Default)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UiState())

    private fun buildState(now: LocalDateTime, settings: Settings): UiState {
        val offset = repository.offsetFor(settings.city)
        val today = now.toLocalDate()
        val slots = repository.slotsFor(today, offset)

        val prayerKeys = setOf(
            PrayerKey.FAJR, PrayerKey.DHUHR, PrayerKey.ASR,
            PrayerKey.MAGHRIB, PrayerKey.ISHA,
        )
        val next = slots
            .filter { it.key in prayerKeys }
            .map { NextPrayer(it.key, it.time.atDate(today)) }
            .firstOrNull { it.at.isAfter(now) }
            ?: repository.slotsFor(today.plusDays(1), offset)
                .firstOrNull { it.key == PrayerKey.FAJR }
                ?.let { NextPrayer(it.key, it.time.atDate(today.plusDays(1))) }

        // The prayer whose time most recently passed; before Sabahu it is
        // still Jacia (whose period runs past midnight).
        val current = slots
            .filter { it.key in prayerKeys }
            .lastOrNull { !it.time.atDate(today).isAfter(now) }
            ?.key
            ?: PrayerKey.ISHA

        // For one minute from the moment a prayer time arrives, the board
        // shows a full-screen announcement instead of the schedule.
        val announce = slots
            .filter { it.key in prayerKeys }
            .firstOrNull {
                val at = it.time.atDate(today)
                !now.isBefore(at) && now.isBefore(at.plusMinutes(1))
            }

        // Overnight energy saver: the mosque is empty between Isha and Imsak,
        // so the board drops to the black theme and dims. The delay after Isha
        // keeps the normal board up while the congregation is still praying.
        val night = settings.nightMode.minutesAfterIsha?.let { delayMinutes ->
            val nightStart = slots.firstOrNull { it.key == PrayerKey.ISHA }
                ?.time?.atDate(today)?.plusMinutes(delayMinutes)
            val nightEnd = slots.firstOrNull { it.key == PrayerKey.IMSAK }
                ?.time?.atDate(today)
            nightStart != null && nightEnd != null &&
                (!now.isBefore(nightStart) || now.isBefore(nightEnd))
        } ?: false

        val hijrah = HijrahDate.from(today)
        val hijri = HijriDate(
            day = hijrah.get(ChronoField.DAY_OF_MONTH),
            month = hijrah.get(ChronoField.MONTH_OF_YEAR),
            year = hijrah.get(ChronoField.YEAR),
        )

        val notices = buildNotices(now, today, slots, hijri)

        // Recurring lecture (e.g. "Zgjimi i Zemrave"): pinned all day on the
        // configured weekday, announcing it follows the configured prayer.
        val lecture = settings.lectureDay.dayOfWeek
            ?.takeIf { it == today.dayOfWeek && settings.lectureTitle.isNotBlank() }
            ?.let {
                LectureInfo(
                    title = settings.lectureTitle,
                    prayer = settings.lecturePrayer,
                    time = slots.firstOrNull { s -> s.key == settings.lecturePrayer }?.time,
                )
            }

        // Islamic events shift on the Gregorian calendar every year, so the
        // bundled dates are only valid for the takvim's own year.
        val upcomingEvent = if (today.year == repository.data.metadata.year) {
            repository.data.metadata.islamicEvents
                .filterKeys { it !in hiddenEvents }
                .mapNotNull { (key, date) ->
                    runCatching { UpcomingEvent(key, LocalDate.parse(date)) }.getOrNull()
                }
                .filter { !it.date.isBefore(today) }
                .minByOrNull { it.date }
                ?.takeIf { Duration.between(today.atStartOfDay(), it.date.atStartOfDay()).toDays() <= 30 }
        } else null

        return UiState(
            now = now,
            slots = slots,
            current = current,
            next = next,
            announce = announce,
            countdown = next?.let { Duration.between(now, it.at) } ?: Duration.ZERO,
            hijri = hijri,
            upcomingEvent = upcomingEvent,
            notices = notices,
            lecture = lecture,
            night = night,
            settings = settings,
            loaded = true,
        )
    }

    private fun buildNotices(
        now: LocalDateTime,
        today: LocalDate,
        slots: List<PrayerSlot>,
        hijri: HijriDate,
    ): List<Notice> = buildList {
        val time = now.toLocalTime()
        fun timeOf(key: PrayerKey) = slots.firstOrNull { it.key == key }?.time

        val dhuhr = timeOf(PrayerKey.DHUHR)
        val friday = today.dayOfWeek == DayOfWeek.FRIDAY

        // Around the khutbah nothing else matters: the silence notice
        // replaces every other one.
        val khutbah = friday && dhuhr != null &&
            !time.isBefore(dhuhr.minusMinutes(20)) && time.isBefore(dhuhr.plusMinutes(45))
        if (khutbah) {
            add(Notice(NoticeKey.FRIDAY_KHUTBAH))
            return@buildList
        }

        val sunrise = timeOf(PrayerKey.SUNRISE)
        val asr = timeOf(PrayerKey.ASR)
        val maghrib = timeOf(PrayerKey.MAGHRIB)

        if (friday && (maghrib == null || time.isBefore(maghrib))) {
            add(Notice(NoticeKey.FRIDAY_KAHF))
            add(Notice(NoticeKey.FRIDAY_DUA))
            // Preparation sunnahs only make sense before Jumu'ah itself.
            if (dhuhr != null && time.isBefore(dhuhr.minusMinutes(20))) {
                add(Notice(NoticeKey.FRIDAY_SUNNAH))
            }
        }

        val fajr = timeOf(PrayerKey.FAJR)
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

        if (asr != null && maghrib != null &&
            !time.isBefore(asr) && time.isBefore(maghrib)
        ) {
            add(Notice(NoticeKey.EVENING_DHIKR))
        }

        // Sunnah-fasting reminders for the evening before, from Asr onwards.
        if (asr != null && !time.isBefore(asr)) {
            when (today.dayOfWeek) {
                DayOfWeek.SUNDAY -> add(Notice(NoticeKey.FAST_MONDAY))
                DayOfWeek.WEDNESDAY -> add(Notice(NoticeKey.FAST_THURSDAY))
                else -> {}
            }
            // The White Days (13/14/15 of the lunar month) each get a
            // reminder the evening before.
            if (hijri.day in 12..14) {
                add(Notice(NoticeKey.FAST_WHITE_DAYS, arg = hijri.day + 1))
            }
        }
    }

    fun setMosqueName(value: String) = viewModelScope.launch { settingsRepository.setMosqueName(value) }
    fun setPlace(value: String) = viewModelScope.launch { settingsRepository.setPlace(value) }
    fun setCity(value: String) = viewModelScope.launch { settingsRepository.setCity(value) }
    fun setOrientation(value: DisplayOrientation) = viewModelScope.launch { settingsRepository.setOrientation(value) }
    fun setLanguage(value: AppLanguage) = viewModelScope.launch { settingsRepository.setLanguage(value) }
    fun setTheme(value: AppTheme) = viewModelScope.launch { settingsRepository.setTheme(value) }
    fun setNightMode(value: NightMode) = viewModelScope.launch { settingsRepository.setNightMode(value) }
    fun setLectureTitle(value: String) = viewModelScope.launch { settingsRepository.setLectureTitle(value) }
    fun setLectureDay(value: LectureDay) = viewModelScope.launch { settingsRepository.setLectureDay(value) }
    fun setLecturePrayer(value: PrayerKey) = viewModelScope.launch { settingsRepository.setLecturePrayer(value) }
}
