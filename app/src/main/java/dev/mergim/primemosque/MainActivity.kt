package dev.mergim.primemosque

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.mergim.primemosque.data.DisplayOrientation
import dev.mergim.primemosque.ui.AdhanCountdownScreen
import dev.mergim.primemosque.ui.AdhanDuaScreen
import dev.mergim.primemosque.ui.AdhanPhase
import dev.mergim.primemosque.ui.AnnouncementScreen
import dev.mergim.primemosque.ui.DisplayScreen
import dev.mergim.primemosque.ui.KhutbahScreen
import dev.mergim.primemosque.ui.PrayerViewModel
import dev.mergim.primemosque.ui.RotatedLayout
import dev.mergim.primemosque.ui.SettingsScreen
import dev.mergim.primemosque.ui.SetupScreen
import dev.mergim.primemosque.ui.stringsFor
import dev.mergim.primemosque.ui.theme.PrimeMosqueTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Digital-signage display: never let the screen sleep.
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContent {
            PrimeMosqueApp()
        }
    }
}

// Backlight level while the night energy saver is active. Black pixels only
// save power on OLED panels; on LCD the backlight is the real consumer.
private const val NIGHT_BRIGHTNESS = 0.1f

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun PrimeMosqueApp(viewModel: PrayerViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    var showSettings by remember { mutableStateOf(false) }
    // The board follows the effective language (which may alternate between
    // two languages); the settings and setup screens stay in the primary so
    // they don't switch mid-configuration.
    val strings = stringsFor(
        if (showSettings || !state.settings.setupDone) state.settings.language
        else state.language
    )
    val orientation = state.settings.orientation

    // Night energy saver: the chosen theme's dark variant + dimmed backlight
    // between Isha and Imsak. Suspended while the settings are open, so the
    // chosen theme stays visible while configuring.
    val nightSaver = state.night && !showSettings
    val view = LocalView.current
    LaunchedEffect(nightSaver) {
        view.context.findActivity()?.window?.let { window ->
            val attributes = window.attributes
            attributes.screenBrightness =
                if (nightSaver) NIGHT_BRIGHTNESS
                else WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            window.attributes = attributes
        }
    }

    PrimeMosqueTheme(theme = if (nightSaver) state.theme.darkVariant else state.theme) {
        Surface(modifier = Modifier.fillMaxSize()) {
            RotatedLayout(degrees = orientation.degrees) {
                val adhan = state.adhan
                when {
                    // First-run setup wizard, once settings have loaded.
                    state.loaded && !state.settings.setupDone -> SetupScreen(
                        state = state,
                        strings = strings,
                        viewModel = viewModel,
                        onDone = viewModel::completeSetup,
                    )
                    showSettings -> SettingsScreen(
                        state = state,
                        strings = strings,
                        viewModel = viewModel,
                        onClose = { showSettings = false },
                    )
                    // The full-screen sequence around a prayer time:
                    // countdown, the adhan itself, then its dua.
                    adhan != null -> when (adhan.phase) {
                        AdhanPhase.COUNTDOWN ->
                            AdhanCountdownScreen(adhan, state, strings) { showSettings = true }

                        AdhanPhase.ADHAN ->
                            AnnouncementScreen(adhan, state, strings) { showSettings = true }

                        AdhanPhase.DUA ->
                            AdhanDuaScreen(state, strings) { showSettings = true }
                    }
                    state.khutbah -> KhutbahScreen(
                        state = state,
                        strings = strings,
                        onOpenSettings = { showSettings = true },
                    )
                    else -> DisplayScreen(
                        state = state,
                        strings = strings,
                        portrait = orientation == DisplayOrientation.PORTRAIT ||
                            orientation == DisplayOrientation.PORTRAIT_REVERSED,
                        onOpenSettings = { showSettings = true },
                    )
                }
            }
        }
    }
}
