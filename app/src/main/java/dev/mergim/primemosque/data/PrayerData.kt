package dev.mergim.primemosque.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrayerDataFile(
    val metadata: Metadata = Metadata(),
    @SerialName("prayer_times") val prayerTimes: Map<String, List<DayEntry>> = emptyMap(),
)

@Serializable
data class Metadata(
    val source: String = "",
    val year: Int = 0,
    @SerialName("hijri_year") val hijriYear: String = "",
    @SerialName("city_offsets_minutes") val cityOffsetsMinutes: Map<String, Int> = emptyMap(),
    @SerialName("islamic_events_2026") val islamicEvents: Map<String, String> = emptyMap(),
)

@Serializable
data class DayEntry(
    val day: Int,
    val date: String = "",
    @SerialName("day_of_week") val dayOfWeek: String = "",
    val imsak: String,
    val fajr: String,
    val sunrise: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    @SerialName("day_length") val dayLength: String = "",
)
