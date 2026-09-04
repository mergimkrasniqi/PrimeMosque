package dev.mergim.primemosque.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mergim.primemosque.data.ADJUSTABLE_PRAYERS
import dev.mergim.primemosque.data.AppLanguage
import dev.mergim.primemosque.data.AppTheme
import dev.mergim.primemosque.data.DisplayOrientation
import dev.mergim.primemosque.data.LectureDay
import dev.mergim.primemosque.data.NightMode
import dev.mergim.primemosque.data.PrayerKey
import dev.mergim.primemosque.ui.theme.LocalBoardPalette

private enum class SettingsSubPage { MAIN, MOSQUE, DISPLAY, FRIDAY, LECTURE, ADJUSTMENTS, ANNOUNCEMENTS }

@Composable
fun SettingsScreen(
    state: UiState,
    strings: Strings,
    viewModel: PrayerViewModel,
    onClose: () -> Unit,
) {
    var page by remember { mutableStateOf(SettingsSubPage.MAIN) }
    val backToMain = { page = SettingsSubPage.MAIN }
    when (page) {
        SettingsSubPage.MOSQUE -> MosqueSettingsPage(state, strings, viewModel, onBack = backToMain)
        SettingsSubPage.DISPLAY -> DisplaySettingsPage(state, strings, viewModel, onBack = backToMain)
        SettingsSubPage.FRIDAY -> FridaySettingsPage(state, strings, viewModel, onBack = backToMain)
        SettingsSubPage.LECTURE -> LectureSettingsPage(state, strings, viewModel, onBack = backToMain)
        SettingsSubPage.ADJUSTMENTS -> AdjustmentsSettingsPage(state, strings, viewModel, onBack = backToMain)
        SettingsSubPage.ANNOUNCEMENTS -> AnnouncementsSettingsPage(state, strings, viewModel, onBack = backToMain)
        SettingsSubPage.MAIN -> MainSettingsPage(
            state = state,
            strings = strings,
            viewModel = viewModel,
            onOpenPage = { page = it },
            onClose = onClose,
        )
    }
}

/**
 * First-run setup: the essential settings presented once, so a new mosque
 * can configure the board without discovering the settings screen.
 */
@Composable
fun SetupScreen(
    state: UiState,
    strings: Strings,
    viewModel: PrayerViewModel,
    onDone: () -> Unit,
) {
    val settings = state.settings
    val cities = viewModel.cities
    val cityIndex = cities.indexOfFirst { it.name == settings.city }.coerceAtLeast(0)
    val orientations = DisplayOrientation.entries
    val orientationIndex = orientations.indexOf(settings.orientation)
    val languages = AppLanguage.entries
    val languageIndex = languages.indexOf(settings.language)
    val themes = AppTheme.entries
    val themeIndex = themes.indexOf(settings.theme)

    SettingsPage(title = strings.setupTitle, rowCount = 7) { rowModifier ->
        CyclerRow(
            modifier = rowModifier(0),
            label = strings.languageLabel,
            value = strings.languageName,
            onPrevious = {
                viewModel.setLanguage(languages[(languageIndex - 1 + languages.size) % languages.size])
            },
            onNext = {
                viewModel.setLanguage(languages[(languageIndex + 1) % languages.size])
            },
        )
        CyclerRow(
            modifier = rowModifier(1),
            label = strings.cityLabel,
            value = cities[cityIndex].let { city ->
                if (city.offsetMinutes == 0) city.name
                else "${city.name} (%+d ${strings.minutesShort})".format(city.offsetMinutes)
            },
            onPrevious = {
                viewModel.setCity(cities[(cityIndex - 1 + cities.size) % cities.size].name)
            },
            onNext = {
                viewModel.setCity(cities[(cityIndex + 1) % cities.size].name)
            },
        )
        EditTextRow(
            label = strings.mosqueNameLabel,
            value = settings.mosqueName,
            strings = strings,
            onSave = viewModel::setMosqueName,
            modifier = rowModifier(2),
        )
        EditTextRow(
            label = strings.placeLabel,
            value = settings.place,
            strings = strings,
            onSave = viewModel::setPlace,
            modifier = rowModifier(3),
        )
        CyclerRow(
            modifier = rowModifier(4),
            label = strings.orientationLabel,
            value = strings.orientationNames[settings.orientation] ?: settings.orientation.name,
            onPrevious = {
                viewModel.setOrientation(
                    orientations[(orientationIndex - 1 + orientations.size) % orientations.size]
                )
            },
            onNext = {
                viewModel.setOrientation(orientations[(orientationIndex + 1) % orientations.size])
            },
        )
        CyclerRow(
            modifier = rowModifier(5),
            label = strings.themeLabel,
            value = strings.themeNames[settings.theme] ?: settings.theme.name,
            onPrevious = {
                viewModel.setTheme(themes[(themeIndex - 1 + themes.size) % themes.size])
            },
            onNext = {
                viewModel.setTheme(themes[(themeIndex + 1) % themes.size])
            },
        )
        Button(
            onClick = onDone,
            modifier = rowModifier(6).fillMaxWidth(),
        ) {
            Text(strings.setupStart, fontSize = 18.sp)
        }
    }
}

/**
 * Shared scaffold for a settings page: gradient background, centered
 * scrollable column, title, and an explicit D-pad focus chain (the UI is
 * drawn rotated on portrait-mounted TVs, which breaks Compose's geometric
 * bounds-based focus search).
 */
@Composable
private fun SettingsPage(
    title: String,
    rowCount: Int,
    content: @Composable (rowModifier: (Int) -> Modifier) -> Unit,
) {
    val palette = LocalBoardPalette.current
    val focusRequesters = remember(rowCount) { List(rowCount) { FocusRequester() } }
    fun rowModifier(index: Int): Modifier = Modifier
        .focusRequester(focusRequesters[index])
        .focusProperties {
            up = if (index > 0) focusRequesters[index - 1] else FocusRequester.Cancel
            down = if (index < focusRequesters.lastIndex) focusRequesters[index + 1] else FocusRequester.Cancel
            left = FocusRequester.Cancel
            right = FocusRequester.Cancel
        }
    LaunchedEffect(Unit) { focusRequesters[0].requestFocus() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(palette.bgTop, palette.bgBottom))),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 560.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = title,
                color = palette.accent,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
            )
            content(::rowModifier)
        }
    }
}

@Composable
private fun MainSettingsPage(
    state: UiState,
    strings: Strings,
    viewModel: PrayerViewModel,
    onOpenPage: (SettingsSubPage) -> Unit,
    onClose: () -> Unit,
) {
    BackHandler(onBack = onClose)

    val settings = state.settings
    val languages = AppLanguage.entries
    val languageIndex = languages.indexOf(settings.language)

    val displaySummary =
        "${strings.orientationNames[settings.orientation] ?: settings.orientation.name} • " +
            (strings.themeNames[settings.theme] ?: settings.theme.name)
    val jumuah = settings.jumuahMinutes
    val fridaySummary = buildString {
        append(
            if (jumuah < 0) strings.jumuahFollowDhuhr
            else "%02d:%02d".format(jumuah / 60, jumuah % 60)
        )
        append(" • ${settings.khutbahMinutes} ${strings.minutesShort}")
    }
    val lectureSummary = buildString {
        append(strings.lectureDayNames[settings.lectureDay] ?: settings.lectureDay.name)
        if (settings.lectureDay != LectureDay.OFF) {
            append(" • ")
            append(strings.prayerNames[settings.lecturePrayer] ?: settings.lecturePrayer.name)
        }
    }
    val adjustSummary = settings.prayerAdjustments
        .filterValues { it != 0 }
        .entries
        .joinToString(" • ") { (key, minutes) ->
            "${strings.prayerNames[key] ?: key.name} %+d".format(minutes)
        }
        .ifEmpty { "0" }
    val announcementsSummary = listOf(settings.announcement1, settings.announcement2)
        .count { it.isNotBlank() }
        .toString()

    SettingsPage(title = strings.settingsTitle, rowCount = 8) { rowModifier ->
        CyclerRow(
            modifier = rowModifier(0),
            label = strings.languageLabel,
            value = strings.languageName,
            onPrevious = {
                viewModel.setLanguage(languages[(languageIndex - 1 + languages.size) % languages.size])
            },
            onNext = {
                viewModel.setLanguage(languages[(languageIndex + 1) % languages.size])
            },
        )
        NavRow(
            modifier = rowModifier(1),
            label = strings.mosqueSectionLabel,
            value = settings.mosqueName,
            onOpen = { onOpenPage(SettingsSubPage.MOSQUE) },
        )
        NavRow(
            modifier = rowModifier(2),
            label = strings.displaySectionLabel,
            value = displaySummary,
            onOpen = { onOpenPage(SettingsSubPage.DISPLAY) },
        )
        NavRow(
            modifier = rowModifier(3),
            label = strings.fridaySectionLabel,
            value = fridaySummary,
            onOpen = { onOpenPage(SettingsSubPage.FRIDAY) },
        )
        NavRow(
            modifier = rowModifier(4),
            label = strings.lectureSectionLabel,
            value = lectureSummary,
            onOpen = { onOpenPage(SettingsSubPage.LECTURE) },
        )
        NavRow(
            modifier = rowModifier(5),
            label = strings.announcementsLabel,
            value = announcementsSummary,
            onOpen = { onOpenPage(SettingsSubPage.ANNOUNCEMENTS) },
        )
        NavRow(
            modifier = rowModifier(6),
            label = strings.adjustSectionLabel,
            value = adjustSummary,
            onOpen = { onOpenPage(SettingsSubPage.ADJUSTMENTS) },
        )
        Button(
            onClick = onClose,
            modifier = rowModifier(7).fillMaxWidth(),
        ) {
            Text(strings.done, fontSize = 18.sp)
        }
        AboutLine()
    }
}

@Composable
private fun MosqueSettingsPage(
    state: UiState,
    strings: Strings,
    viewModel: PrayerViewModel,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)

    val settings = state.settings
    val cities = viewModel.cities
    val cityIndex = cities.indexOfFirst { it.name == settings.city }.coerceAtLeast(0)

    SettingsPage(title = strings.mosqueSectionLabel, rowCount = 4) { rowModifier ->
        EditTextRow(
            label = strings.mosqueNameLabel,
            value = settings.mosqueName,
            strings = strings,
            onSave = viewModel::setMosqueName,
            modifier = rowModifier(0),
        )
        EditTextRow(
            label = strings.placeLabel,
            value = settings.place,
            strings = strings,
            onSave = viewModel::setPlace,
            modifier = rowModifier(1),
        )
        CyclerRow(
            modifier = rowModifier(2),
            label = strings.cityLabel,
            value = cities[cityIndex].let { city ->
                if (city.offsetMinutes == 0) city.name
                else "${city.name} (%+d ${strings.minutesShort})".format(city.offsetMinutes)
            },
            onPrevious = {
                viewModel.setCity(cities[(cityIndex - 1 + cities.size) % cities.size].name)
            },
            onNext = {
                viewModel.setCity(cities[(cityIndex + 1) % cities.size].name)
            },
        )
        Button(
            onClick = onBack,
            modifier = rowModifier(3).fillMaxWidth(),
        ) {
            Text(strings.back, fontSize = 18.sp)
        }
    }
}

@Composable
private fun DisplaySettingsPage(
    state: UiState,
    strings: Strings,
    viewModel: PrayerViewModel,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)

    val settings = state.settings
    val orientations = DisplayOrientation.entries
    val orientationIndex = orientations.indexOf(settings.orientation)
    val themes = AppTheme.entries
    val themeIndex = themes.indexOf(settings.theme)
    val nightModes = NightMode.entries
    val nightModeIndex = nightModes.indexOf(settings.nightMode)

    SettingsPage(title = strings.displaySectionLabel, rowCount = 6) { rowModifier ->
        CyclerRow(
            modifier = rowModifier(0),
            label = strings.orientationLabel,
            value = strings.orientationNames[settings.orientation] ?: settings.orientation.name,
            onPrevious = {
                viewModel.setOrientation(
                    orientations[(orientationIndex - 1 + orientations.size) % orientations.size]
                )
            },
            onNext = {
                viewModel.setOrientation(orientations[(orientationIndex + 1) % orientations.size])
            },
        )
        CyclerRow(
            modifier = rowModifier(1),
            label = strings.themeLabel,
            value = strings.themeNames[settings.theme] ?: settings.theme.name,
            onPrevious = {
                viewModel.setTheme(themes[(themeIndex - 1 + themes.size) % themes.size])
            },
            onNext = {
                viewModel.setTheme(themes[(themeIndex + 1) % themes.size])
            },
        )
        CyclerRow(
            modifier = rowModifier(2),
            label = strings.themeRotationLabel,
            value = if (settings.themeRotation) strings.themeRotationWeekly
            else strings.themeRotationOff,
            onPrevious = { viewModel.setThemeRotation(!settings.themeRotation) },
            onNext = { viewModel.setThemeRotation(!settings.themeRotation) },
        )
        CyclerRow(
            modifier = rowModifier(3),
            label = strings.dailyQuotesLabel,
            value = if (settings.showDailyQuotes) strings.switchOn else strings.switchOff,
            onPrevious = { viewModel.setShowDailyQuotes(!settings.showDailyQuotes) },
            onNext = { viewModel.setShowDailyQuotes(!settings.showDailyQuotes) },
        )
        CyclerRow(
            modifier = rowModifier(4),
            label = strings.nightModeLabel,
            value = strings.nightModeNames[settings.nightMode] ?: settings.nightMode.name,
            onPrevious = {
                viewModel.setNightMode(
                    nightModes[(nightModeIndex - 1 + nightModes.size) % nightModes.size]
                )
            },
            onNext = {
                viewModel.setNightMode(nightModes[(nightModeIndex + 1) % nightModes.size])
            },
        )
        Button(
            onClick = onBack,
            modifier = rowModifier(5).fillMaxWidth(),
        ) {
            Text(strings.back, fontSize = 18.sp)
        }
    }
}

@Composable
private fun FridaySettingsPage(
    state: UiState,
    strings: Strings,
    viewModel: PrayerViewModel,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)

    val settings = state.settings
    val jumuah = settings.jumuahMinutes
    val khutbahMinutes = settings.khutbahMinutes

    SettingsPage(title = strings.fridaySectionLabel, rowCount = 3) { rowModifier ->
        CyclerRow(
            modifier = rowModifier(0),
            label = strings.jumuahLabel,
            value = if (jumuah < 0) {
                strings.jumuahFollowDhuhr
            } else {
                "%02d:%02d".format(jumuah / 60, jumuah % 60)
            },
            onPrevious = {
                viewModel.setJumuahMinutes(
                    when {
                        jumuah < 0 -> 15 * 60
                        jumuah <= 12 * 60 -> -1
                        else -> jumuah - 5
                    }
                )
            },
            onNext = {
                viewModel.setJumuahMinutes(
                    when {
                        jumuah < 0 -> 12 * 60
                        jumuah >= 15 * 60 -> -1
                        else -> jumuah + 5
                    }
                )
            },
        )
        CyclerRow(
            modifier = rowModifier(1),
            label = strings.khutbahDurationLabel,
            value = "$khutbahMinutes ${strings.minutesShort}",
            onPrevious = { viewModel.setKhutbahMinutes(khutbahMinutes - 5) },
            onNext = { viewModel.setKhutbahMinutes(khutbahMinutes + 5) },
        )
        Button(
            onClick = onBack,
            modifier = rowModifier(2).fillMaxWidth(),
        ) {
            Text(strings.back, fontSize = 18.sp)
        }
    }
}

/** App name + version at the bottom of the settings, for support questions. */
@Composable
private fun AboutLine() {
    val context = LocalContext.current
    val version = remember {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull() ?: ""
    }
    Text(
        text = "PrimeMosque v$version",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 13.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun AnnouncementsSettingsPage(
    state: UiState,
    strings: Strings,
    viewModel: PrayerViewModel,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)

    val settings = state.settings
    SettingsPage(title = strings.announcementsLabel, rowCount = 3) { rowModifier ->
        EditTextRow(
            label = "${strings.announcementLabel} 1",
            value = settings.announcement1,
            strings = strings,
            onSave = viewModel::setAnnouncement1,
            modifier = rowModifier(0),
        )
        EditTextRow(
            label = "${strings.announcementLabel} 2",
            value = settings.announcement2,
            strings = strings,
            onSave = viewModel::setAnnouncement2,
            modifier = rowModifier(1),
        )
        Button(
            onClick = onBack,
            modifier = rowModifier(2).fillMaxWidth(),
        ) {
            Text(strings.back, fontSize = 18.sp)
        }
    }
}

@Composable
private fun AdjustmentsSettingsPage(
    state: UiState,
    strings: Strings,
    viewModel: PrayerViewModel,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)

    val adjustments = state.settings.prayerAdjustments
    val hijriOffset = state.settings.hijriOffset

    // 6 prayer rows + hijri date + reset + back.
    SettingsPage(title = strings.adjustSectionLabel, rowCount = ADJUSTABLE_PRAYERS.size + 3) { rowModifier ->
        ADJUSTABLE_PRAYERS.forEachIndexed { index, key ->
            val minutes = adjustments[key] ?: 0
            CyclerRow(
                modifier = rowModifier(index),
                label = strings.prayerNames[key] ?: key.name,
                value = if (minutes == 0) "0" else "%+d ${strings.minutesShort}".format(minutes),
                onPrevious = { viewModel.setPrayerAdjustment(key, minutes - 1) },
                onNext = { viewModel.setPrayerAdjustment(key, minutes + 1) },
            )
        }
        CyclerRow(
            modifier = rowModifier(ADJUSTABLE_PRAYERS.size),
            label = strings.hijriOffsetLabel,
            value = if (hijriOffset == 0) "0" else "%+d".format(hijriOffset),
            onPrevious = { viewModel.setHijriOffset(hijriOffset - 1) },
            onNext = { viewModel.setHijriOffset(hijriOffset + 1) },
        )
        Button(
            onClick = viewModel::resetPrayerAdjustments,
            modifier = rowModifier(ADJUSTABLE_PRAYERS.size + 1).fillMaxWidth(),
        ) {
            Text(strings.adjustResetLabel, fontSize = 18.sp)
        }
        Button(
            onClick = onBack,
            modifier = rowModifier(ADJUSTABLE_PRAYERS.size + 2).fillMaxWidth(),
        ) {
            Text(strings.back, fontSize = 18.sp)
        }
    }
}

@Composable
private fun LectureSettingsPage(
    state: UiState,
    strings: Strings,
    viewModel: PrayerViewModel,
    onBack: () -> Unit,
) {
    BackHandler(onBack = onBack)

    val settings = state.settings
    val lectureDays = LectureDay.entries
    val lectureDayIndex = lectureDays.indexOf(settings.lectureDay)
    val lecturePrayers = listOf(
        PrayerKey.FAJR, PrayerKey.DHUHR, PrayerKey.ASR,
        PrayerKey.MAGHRIB, PrayerKey.ISHA,
    )
    val lecturePrayerIndex = lecturePrayers.indexOf(settings.lecturePrayer).coerceAtLeast(0)

    SettingsPage(title = strings.lectureSectionLabel, rowCount = 4) { rowModifier ->
        EditTextRow(
            label = strings.lectureTitleLabel,
            value = settings.lectureTitle,
            strings = strings,
            onSave = viewModel::setLectureTitle,
            modifier = rowModifier(0),
        )
        CyclerRow(
            modifier = rowModifier(1),
            label = strings.lectureDayLabel,
            value = strings.lectureDayNames[settings.lectureDay] ?: settings.lectureDay.name,
            onPrevious = {
                viewModel.setLectureDay(
                    lectureDays[(lectureDayIndex - 1 + lectureDays.size) % lectureDays.size]
                )
            },
            onNext = {
                viewModel.setLectureDay(lectureDays[(lectureDayIndex + 1) % lectureDays.size])
            },
        )
        CyclerRow(
            modifier = rowModifier(2),
            label = strings.lecturePrayerLabel,
            value = strings.prayerNames[settings.lecturePrayer] ?: settings.lecturePrayer.name,
            onPrevious = {
                viewModel.setLecturePrayer(
                    lecturePrayers[(lecturePrayerIndex - 1 + lecturePrayers.size) % lecturePrayers.size]
                )
            },
            onNext = {
                viewModel.setLecturePrayer(lecturePrayers[(lecturePrayerIndex + 1) % lecturePrayers.size])
            },
        )
        Button(
            onClick = onBack,
            modifier = rowModifier(3).fillMaxWidth(),
        ) {
            Text(strings.back, fontSize = 18.sp)
        }
    }
}

/**
 * TV-friendly text setting: a focusable row showing the current value.
 * Pressing OK opens a dialog with a text field, so the on-screen keyboard
 * only appears when the user actually wants to edit.
 */
@Composable
private fun EditTextRow(
    label: String,
    value: String,
    strings: Strings,
    onSave: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var focused by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf(false) }
    val palette = LocalBoardPalette.current
    val shape = RoundedCornerShape(12.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(palette.cardHighlight, shape)
            .then(if (focused) Modifier.border(2.dp, palette.accent, shape) else Modifier)
            .onFocusChanged { focused = it.isFocused }
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown &&
                    (event.key == Key.DirectionCenter || event.key == Key.Enter)
                ) {
                    editing = true
                    true
                } else {
                    false
                }
            }
            .focusable()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = value.ifBlank { "—" },
                color = if (focused) palette.accent else MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
            )
        }
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    if (editing) {
        var draft by remember { mutableStateOf(value) }
        val fieldFocus = remember { FocusRequester() }
        AlertDialog(
            onDismissRequest = { editing = false },
            title = { Text(label) },
            text = {
                OutlinedTextField(
                    value = draft,
                    onValueChange = { draft = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(fieldFocus),
                )
                LaunchedEffect(Unit) { fieldFocus.requestFocus() }
            },
            confirmButton = {
                TextButton(onClick = {
                    onSave(draft.trim())
                    editing = false
                }) { Text(strings.save) }
            },
            dismissButton = {
                TextButton(onClick = { editing = false }) { Text(strings.cancel) }
            },
        )
    }
}

/**
 * TV-friendly value picker: focus the row with the D-pad, then press
 * left/right (or OK) to cycle through the values.
 */
@Composable
private fun CyclerRow(
    label: String,
    value: String,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var focused by remember { mutableStateOf(false) }
    val palette = LocalBoardPalette.current
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(palette.cardHighlight, shape)
            .then(
                if (focused) Modifier.border(2.dp, palette.accent, shape) else Modifier
            )
            .onFocusChanged { focused = it.isFocused }
            .onKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
                when (event.key) {
                    Key.DirectionLeft -> {
                        onPrevious(); true
                    }
                    Key.DirectionRight, Key.DirectionCenter, Key.Enter -> {
                        onNext(); true
                    }
                    else -> false
                }
            }
            .focusable()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = value,
                color = if (focused) palette.accent else MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
            )
        }
        Icon(
            imageVector = Icons.Filled.ChevronLeft,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * Navigation row that opens a sub-page of settings when pressed with OK
 * (or D-pad right).
 */
@Composable
private fun NavRow(
    label: String,
    value: String,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var focused by remember { mutableStateOf(false) }
    val palette = LocalBoardPalette.current
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(palette.cardHighlight, shape)
            .then(if (focused) Modifier.border(2.dp, palette.accent, shape) else Modifier)
            .onFocusChanged { focused = it.isFocused }
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown &&
                    (event.key == Key.DirectionCenter || event.key == Key.Enter ||
                        event.key == Key.DirectionRight)
                ) {
                    onOpen()
                    true
                } else {
                    false
                }
            }
            .focusable()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = value,
                color = if (focused) palette.accent else MaterialTheme.colorScheme.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
            )
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
