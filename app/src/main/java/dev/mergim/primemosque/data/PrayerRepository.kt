package dev.mergim.primemosque.data

import android.content.Context
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/** ZAWAL is the astronomical noon from the takvim, shown as a sub-time of DHUHR. */
enum class PrayerKey { IMSAK, FAJR, SUNRISE, DHUHR, ZAWAL, ASR, MAGHRIB, ISHA }

data class PrayerSlot(val key: PrayerKey, val time: LocalTime)

data class City(val name: String, val offsetMinutes: Int)

/**
 * Loads the bundled BIK Kosovo takvim (yearly prayer times). The takvim repeats
 * every year, so lookups are done by month + day-of-month, ignoring the year.
 */
class PrayerRepository(context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    private val zone = ZoneId.of("Europe/Belgrade")

    private val timeFormat = DateTimeFormatter.ofPattern("H:mm")

    val data: PrayerDataFile = context.assets
        .open("kosovo-prayer-times.json")
        .bufferedReader()
        .use { it.readText() }
        .let { json.decodeFromString<PrayerDataFile>(it) }

    val cities: List<City> = run {
        val offsets = data.metadata.cityOffsetsMinutes
        val names = listOf(
            "Prizren", "Prishtina", "Peja", "Gjakova", "Mitrovica", "Ferizaj",
            "Gjilan", "Podujeva", "Vushtrri", "Deçan", "Rahovec", "Suhareka",
            "Malisheva", "Drenas", "Skenderaj", "Klina", "Istog", "Lipjan",
            "Fushë Kosova", "Obiliq", "Shtime", "Kaçanik", "Kamenica", "Viti",
            "Dragash (Sharri)", "Presheva",
        )
        names.map { name ->
            val key = if (name.startsWith("Dragash")) "Sharri" else name
            City(name, offsets[key] ?: 0)
        }
    }

    fun offsetFor(cityName: String): Int =
        cities.firstOrNull { it.name == cityName }?.offsetMinutes ?: 0

    fun slotsFor(date: LocalDate, offsetMinutes: Int): List<PrayerSlot> {
        val monthName = date.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
        val month = data.prayerTimes[monthName] ?: return emptyList()
        // Feb 29 in leap years falls back to the last available day of the month.
        val entry = month.firstOrNull { it.day == date.dayOfMonth } ?: month.last()
        val shift = takvimDstShiftMinutes(date, data.metadata.year, entry.day, zone)
        fun t(raw: String) =
            LocalTime.parse(raw, timeFormat).plusMinutes((offsetMinutes + shift).toLong())
        // In Kosovo the congregational Dreka/Xhuma is held at a fixed clock
        // time: 13:00 during daylight saving time, 12:00 in standard time.
        // The astronomical noon from the takvim is kept as the ZAWAL sub-time.
        val dhuhrFixed = if (isDaylightSaving(date, zone)) LocalTime.of(13, 0) else LocalTime.of(12, 0)
        return listOf(
            PrayerSlot(PrayerKey.IMSAK, t(entry.imsak)),
            // In Kosovo the Sabahu prayer is held 40 minutes before sunrise.
            PrayerSlot(PrayerKey.FAJR, t(entry.sunrise).minusMinutes(40)),
            PrayerSlot(PrayerKey.SUNRISE, t(entry.sunrise)),
            PrayerSlot(PrayerKey.DHUHR, dhuhrFixed),
            PrayerSlot(PrayerKey.ZAWAL, t(entry.dhuhr)),
            PrayerSlot(PrayerKey.ASR, t(entry.asr)),
            PrayerSlot(PrayerKey.MAGHRIB, t(entry.maghrib)),
            PrayerSlot(PrayerKey.ISHA, t(entry.isha)),
        )
    }
}

private fun isDaylightSaving(date: LocalDate, zone: ZoneId): Boolean =
    zone.rules.isDaylightSavings(date.atTime(12, 0).atZone(zone).toInstant())

/**
 * Minutes to add to a takvim time for [date]. The takvim gives clock times
 * for its own year ([takvimYear]), with that year's daylight saving already
 * applied, but the clocks change on different dates every year (2026: 29
 * March / 25 October; 2027: 28 March / 31 October). Taken back to standard
 * time by the takvim year's dates and forward again by this year's, the
 * times are right on the switch days of any year. [takvimDay] is the day of
 * the month the entry is for, which differs from [date] on 29 February.
 */
internal fun takvimDstShiftMinutes(
    date: LocalDate,
    takvimYear: Int,
    takvimDay: Int,
    zone: ZoneId,
): Int {
    // Without a known takvim year there is nothing to correct against.
    if (takvimYear <= 0) return 0
    val takvimDate = runCatching { LocalDate.of(takvimYear, date.month, takvimDay) }
        .getOrNull() ?: return 0
    val now = if (isDaylightSaving(date, zone)) 60 else 0
    val then = if (isDaylightSaving(takvimDate, zone)) 60 else 0
    return now - then
}
