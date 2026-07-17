package dev.mergim.primemosque.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.mergim.primemosque.data.AppLanguage
import dev.mergim.primemosque.data.AppTheme
import dev.mergim.primemosque.data.City
import dev.mergim.primemosque.data.DisplayOrientation
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
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.chrono.HijrahDate
import java.time.temporal.ChronoField

data class NextPrayer(val key: PrayerKey, val at: LocalDateTime)

data class HijriDate(val day: Int, val month: Int, val year: Int)

data class UpcomingEvent(val key: String, val date: LocalDate)

data class UiState(
    val now: LocalDateTime = LocalDateTime.now(),
    val slots: List<PrayerSlot> = emptyList(),
    val current: PrayerKey? = null,
    val next: NextPrayer? = null,
    val countdown: Duration = Duration.ZERO,
    val hijri: HijriDate? = null,
    val upcomingEvent: UpcomingEvent? = null,
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

        val hijrah = HijrahDate.from(today)
        val hijri = HijriDate(
            day = hijrah.get(ChronoField.DAY_OF_MONTH),
            month = hijrah.get(ChronoField.MONTH_OF_YEAR),
            year = hijrah.get(ChronoField.YEAR),
        )

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
            countdown = next?.let { Duration.between(now, it.at) } ?: Duration.ZERO,
            hijri = hijri,
            upcomingEvent = upcomingEvent,
            settings = settings,
            loaded = true,
        )
    }

    fun setMosqueName(value: String) = viewModelScope.launch { settingsRepository.setMosqueName(value) }
    fun setPlace(value: String) = viewModelScope.launch { settingsRepository.setPlace(value) }
    fun setCity(value: String) = viewModelScope.launch { settingsRepository.setCity(value) }
    fun setOrientation(value: DisplayOrientation) = viewModelScope.launch { settingsRepository.setOrientation(value) }
    fun setLanguage(value: AppLanguage) = viewModelScope.launch { settingsRepository.setLanguage(value) }
    fun setTheme(value: AppTheme) = viewModelScope.launch { settingsRepository.setTheme(value) }
}
