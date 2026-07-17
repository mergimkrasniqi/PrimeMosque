package dev.mergim.primemosque.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness5
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mergim.primemosque.data.PrayerKey
import dev.mergim.primemosque.data.PrayerSlot
import dev.mergim.primemosque.ui.theme.LocalBoardPalette
import java.time.DayOfWeek
import java.time.Duration
import java.time.format.DateTimeFormatter

private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val clockFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val secondsFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("ss")

private fun Duration.asClock(): String =
    "%02d:%02d:%02d".format(toHours(), toMinutes() % 60, seconds % 60)

/** On Fridays the Dhuhr slot is the Jumu'ah prayer. */
private fun prayerLabel(key: PrayerKey, strings: Strings, friday: Boolean): String =
    if (friday && key == PrayerKey.DHUHR) strings.fridayDhuhrName
    else strings.prayerNames[key] ?: key.name

private fun prayerIcon(key: PrayerKey): ImageVector = when (key) {
    PrayerKey.IMSAK -> Icons.Filled.Bedtime
    PrayerKey.FAJR -> Icons.Filled.WbTwilight
    PrayerKey.SUNRISE -> Icons.Filled.WbSunny
    PrayerKey.DHUHR, PrayerKey.ZAWAL -> Icons.Filled.LightMode
    PrayerKey.ASR -> Icons.Filled.Brightness5
    PrayerKey.MAGHRIB -> Icons.Filled.Brightness4
    PrayerKey.ISHA -> Icons.Filled.DarkMode
}

/** Slots rendered inside another prayer's field instead of as their own row. */
private val subSlotKeys = setOf(PrayerKey.IMSAK, PrayerKey.SUNRISE, PrayerKey.ZAWAL)

private fun subTimesFor(
    slot: PrayerSlot,
    slots: List<PrayerSlot>,
    strings: Strings,
    withLabels: Boolean,
): List<String> {
    fun find(key: PrayerKey) = slots.firstOrNull { it.key == key }
    fun format(key: PrayerKey): String? = find(key)?.let {
        val time = it.time.format(timeFormatter)
        if (withLabels) "${strings.prayerNames[key]} $time" else time
    }
    return when (slot.key) {
        PrayerKey.FAJR -> listOfNotNull(format(PrayerKey.IMSAK), format(PrayerKey.SUNRISE))
        // The astronomical noon is shown without a label, like on MyMosq.
        PrayerKey.DHUHR -> listOfNotNull(find(PrayerKey.ZAWAL)?.time?.format(timeFormatter))
        else -> emptyList()
    }
}

@Composable
fun DisplayScreen(
    state: UiState,
    strings: Strings,
    portrait: Boolean,
    onOpenSettings: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    val palette = LocalBoardPalette.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(palette.bgTop, palette.bgBottom)))
            .focusRequester(focusRequester)
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown &&
                    (event.key == Key.DirectionCenter || event.key == Key.Enter || event.key == Key.Menu)
                ) {
                    onOpenSettings()
                    true
                } else {
                    false
                }
            }
            .focusable(),
    ) {
        if (!state.loaded) return@Box
        if (portrait) PortraitBoard(state, strings) else LandscapeBoard(state, strings)
    }
}

@Composable
private fun PortraitBoard(state: UiState, strings: Strings) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Header(state, strings, centered = true)
        Spacer(Modifier.height(10.dp))
        BigClock(state)
        DateLines(state, strings, centered = true)
        Spacer(Modifier.height(14.dp))
        CountdownBanner(state, strings)
        Spacer(Modifier.height(14.dp))
        val friday = state.now.dayOfWeek == DayOfWeek.FRIDAY
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            state.slots
                .filter { it.key !in subSlotKeys }
                .forEach { slot ->
                    PrayerRow(
                        slot, strings,
                        friday = friday,
                        current = slot.key == state.current,
                        subTimes = subTimesFor(slot, state.slots, strings, withLabels = true),
                    )
                }
        }
        Footer(state, strings)
    }
}

@Composable
private fun LandscapeBoard(state: UiState, strings: Strings) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp, vertical = 24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) { Header(state, strings, centered = false) }
            Column(horizontalAlignment = Alignment.End) {
                DateLines(state, strings, centered = false)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BoxWithConstraints(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                // Scale the clock + countdown to the vertical space actually
                // available, so nothing clips on low-resolution screens.
                val compact = maxHeight < 200.dp
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BigClock(
                        state,
                        mainSize = if (compact) 46.sp else 72.sp,
                        secondsSize = if (compact) 22.sp else 36.sp,
                    )
                    Spacer(Modifier.height(if (compact) 6.dp else 12.dp))
                    CountdownBanner(state, strings, compact = compact)
                }
            }
        }
        val friday = state.now.dayOfWeek == DayOfWeek.FRIDAY
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            state.slots
                .filter { it.key !in subSlotKeys }
                .forEach { slot ->
                    PrayerCard(
                        slot = slot,
                        strings = strings,
                        friday = friday,
                        current = slot.key == state.current,
                        subTimes = subTimesFor(slot, state.slots, strings, withLabels = false),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                }
        }
        Spacer(Modifier.height(12.dp))
        Footer(state, strings)
    }
}

@Composable
private fun Header(state: UiState, strings: Strings, centered: Boolean) {
    val align = if (centered) TextAlign.Center else TextAlign.Start
    Column(
        modifier = if (centered) Modifier.fillMaxWidth() else Modifier,
        horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start,
    ) {
        Text(
            text = state.settings.mosqueName,
            color = LocalBoardPalette.current.accent,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            textAlign = align,
        )
        if (state.settings.place.isNotBlank()) {
            Text(
                text = state.settings.place,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 20.sp,
                textAlign = align,
            )
        }
    }
}

@Composable
private fun BigClock(
    state: UiState,
    mainSize: TextUnit = 92.sp,
    secondsSize: TextUnit = 44.sp,
) {
    Row(verticalAlignment = Alignment.Bottom) {
        Text(
            text = state.now.format(clockFormatter),
            fontSize = mainSize,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = state.now.format(secondsFormatter),
            fontSize = secondsSize,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp, bottom = 14.dp),
        )
    }
}

@Composable
private fun DateLines(state: UiState, strings: Strings, centered: Boolean) {
    val align = if (centered) TextAlign.Center else TextAlign.End
    val gregorian = state.now.toLocalDate()
        .format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", strings.locale))
        .replaceFirstChar { it.uppercase(strings.locale) }
    Text(
        text = gregorian,
        fontSize = 22.sp,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = align,
    )
    state.hijri?.let { hijri ->
        val monthName = strings.hijriMonths.getOrNull(hijri.month - 1) ?: ""
        Text(
            text = "${hijri.day} $monthName ${hijri.year}",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = align,
        )
    }
}

@Composable
private fun CountdownBanner(state: UiState, strings: Strings, compact: Boolean = false) {
    val next = state.next ?: return
    val palette = LocalBoardPalette.current
    val name = prayerLabel(next.key, strings, friday = next.at.dayOfWeek == DayOfWeek.FRIDAY)
    Row(
        modifier = Modifier
            .background(palette.cardHighlight, RoundedCornerShape(50))
            .border(2.dp, palette.accent, RoundedCornerShape(50))
            .padding(
                horizontal = if (compact) 18.dp else 26.dp,
                vertical = if (compact) 6.dp else 10.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = state.countdown.asClock(),
            color = palette.accent,
            fontSize = if (compact) 20.sp else 30.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = " ${strings.nextPrayerIn} $name",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = if (compact) 16.sp else 24.sp,
        )
    }
}

@Composable
private fun PrayerRow(
    slot: PrayerSlot,
    strings: Strings,
    friday: Boolean,
    current: Boolean = false,
    subTimes: List<String> = emptyList(),
) {
    val palette = LocalBoardPalette.current
    val shape = RoundedCornerShape(16.dp)
    val contentColor = MaterialTheme.colorScheme.onBackground
    val subColor = MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (current) palette.cardHighlight else palette.card, shape)
            .then(
                if (current) {
                    Modifier.border(1.dp, palette.accent.copy(alpha = 0.4f), shape)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 22.dp, vertical = 12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = prayerIcon(slot.key),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(30.dp),
            )
            Spacer(Modifier.width(18.dp))
            Text(
                text = prayerLabel(slot.key, strings, friday),
                fontSize = 26.sp,
                color = contentColor,
                fontWeight = FontWeight.Medium,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = slot.time.format(timeFormatter),
                fontSize = 34.sp,
                color = contentColor,
                fontWeight = FontWeight.Bold,
            )
        }
        if (subTimes.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp, top = 2.dp),
                horizontalArrangement = if (subTimes.size == 1) {
                    Arrangement.Center
                } else {
                    Arrangement.SpaceBetween
                },
            ) {
                subTimes.forEach { sub ->
                    Text(
                        text = sub,
                        fontSize = 17.sp,
                        color = subColor,
                    )
                }
            }
        }
    }
}

@Composable
private fun PrayerCard(
    slot: PrayerSlot,
    strings: Strings,
    friday: Boolean,
    current: Boolean = false,
    subTimes: List<String> = emptyList(),
    modifier: Modifier = Modifier,
) {
    val palette = LocalBoardPalette.current
    val shape = RoundedCornerShape(16.dp)
    val contentColor = MaterialTheme.colorScheme.onBackground
    Column(
        modifier = modifier
            .background(if (current) palette.cardHighlight else palette.card, shape)
            .then(
                if (current) {
                    Modifier.border(1.dp, palette.accent.copy(alpha = 0.4f), shape)
                } else {
                    Modifier
                }
            )
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = prayerIcon(slot.key),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(28.dp),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = prayerLabel(slot.key, strings, friday),
            fontSize = 18.sp,
            color = contentColor,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = slot.time.format(timeFormatter),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = contentColor,
        )
        if (subTimes.isNotEmpty()) {
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                subTimes.forEach { sub ->
                    Text(
                        text = sub,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

/**
 * Full-screen announcement shown for one minute from the moment a prayer
 * time arrives ("Koha e Namazit të ...").
 */
@Composable
fun AnnouncementScreen(slot: PrayerSlot, state: UiState, strings: Strings) {
    val palette = LocalBoardPalette.current
    val friday = state.now.dayOfWeek == DayOfWeek.FRIDAY
    val name = if (friday && slot.key == PrayerKey.DHUHR) {
        strings.fridayDhuhrAnnounceName
    } else {
        strings.announceNames[slot.key] ?: slot.key.name
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(palette.bgTop, palette.bgBottom))),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp),
        ) {
            Icon(
                imageVector = prayerIcon(slot.key),
                contentDescription = null,
                tint = palette.accent,
                modifier = Modifier.size(72.dp),
            )
            Spacer(Modifier.height(28.dp))
            Text(
                text = strings.announceTemplate.format(name),
                color = palette.accent,
                fontSize = 46.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = slot.time.format(timeFormatter),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 88.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun Footer(state: UiState, strings: Strings) {
    val palette = LocalBoardPalette.current
    // On Fridays the salawat stays pinned all day; on other days the dhikr
    // reminders rotate every 5 minutes.
    val reminder = if (state.now.dayOfWeek == DayOfWeek.FRIDAY) {
        strings.fridaySalawat
    } else {
        strings.reminders[(state.now.toLocalTime().toSecondOfDay() / (5 * 60)) % strings.reminders.size]
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = reminder.text,
            color = palette.accent,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
        )
        Text(
            text = reminder.translation,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 6.dp),
        )
        state.upcomingEvent?.let { event ->
            val name = strings.eventNames[event.key] ?: event.key
            Text(
                text = "$name • ${event.date.format(DateTimeFormatter.ofPattern("d MMMM", strings.locale))}",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 18.sp,
            )
        }
    }
}
