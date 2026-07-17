package dev.mergim.primemosque.data

import android.content.Context
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

enum class PrayerKey { IMSAK, FAJR, SUNRISE, DHUHR, ASR, MAGHRIB, ISHA }

data class PrayerSlot(val key: PrayerKey, val time: LocalTime)

data class City(val name: String, val offsetMinutes: Int)

/**
 * Loads the bundled BIK Kosovo takvim (yearly prayer times). The takvim repeats
 * every year, so lookups are done by month + day-of-month, ignoring the year.
 */
class PrayerRepository(context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

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
        val fmt = DateTimeFormatter.ofPattern("H:mm")
        fun t(raw: String) = LocalTime.parse(raw, fmt).plusMinutes(offsetMinutes.toLong())
        return listOf(
            PrayerSlot(PrayerKey.IMSAK, t(entry.imsak)),
            PrayerSlot(PrayerKey.FAJR, t(entry.fajr)),
            PrayerSlot(PrayerKey.SUNRISE, t(entry.sunrise)),
            PrayerSlot(PrayerKey.DHUHR, t(entry.dhuhr)),
            PrayerSlot(PrayerKey.ASR, t(entry.asr)),
            PrayerSlot(PrayerKey.MAGHRIB, t(entry.maghrib)),
            PrayerSlot(PrayerKey.ISHA, t(entry.isha)),
        )
    }
}
