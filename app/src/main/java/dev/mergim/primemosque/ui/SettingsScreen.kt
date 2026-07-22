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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.mergim.primemosque.data.AppLanguage
import dev.mergim.primemosque.data.AppTheme
import dev.mergim.primemosque.data.DisplayOrientation
import dev.mergim.primemosque.data.NightMode
import dev.mergim.primemosque.ui.theme.LocalBoardPalette

@Composable
fun SettingsScreen(
    state: UiState,
    strings: Strings,
    viewModel: PrayerViewModel,
    onClose: () -> Unit,
) {
    BackHandler(onBack = onClose)

    val settings = state.settings
    val cities = viewModel.cities
    val cityIndex = cities.indexOfFirst { it.name == settings.city }.coerceAtLeast(0)
    val orientations = DisplayOrientation.entries
    val orientationIndex = orientations.indexOf(settings.orientation)
    val languages = AppLanguage.entries
    val languageIndex = languages.indexOf(settings.language)
    val themes = AppTheme.entries
    val themeIndex = themes.indexOf(settings.theme)
    val nightModes = NightMode.entries
    val nightModeIndex = nightModes.indexOf(settings.nightMode)
    val palette = LocalBoardPalette.current

    // Explicit focus chain: the UI is drawn rotated on portrait-mounted TVs,
    // which breaks Compose's geometric (bounds-based) D-pad focus search.
    val focusRequesters = remember { List(8) { FocusRequester() } }
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
                text = strings.settingsTitle,
                color = palette.accent,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
            )
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
            CyclerRow(
                modifier = rowModifier(3),
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
                modifier = rowModifier(4),
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
            CyclerRow(
                modifier = rowModifier(6),
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
                onClick = onClose,
                modifier = rowModifier(7).fillMaxWidth(),
            ) {
                Text(strings.done, fontSize = 18.sp)
            }
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
