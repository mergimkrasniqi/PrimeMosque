package dev.mergim.primemosque

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.mergim.primemosque.data.DisplayOrientation
import dev.mergim.primemosque.ui.AnnouncementScreen
import dev.mergim.primemosque.ui.DisplayScreen
import dev.mergim.primemosque.ui.PrayerViewModel
import dev.mergim.primemosque.ui.RotatedLayout
import dev.mergim.primemosque.ui.SettingsScreen
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

@Composable
fun PrimeMosqueApp(viewModel: PrayerViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    val strings = stringsFor(state.settings.language)
    var showSettings by remember { mutableStateOf(false) }
    val orientation = state.settings.orientation

    PrimeMosqueTheme(theme = state.settings.theme) {
        Surface(modifier = Modifier.fillMaxSize()) {
            RotatedLayout(degrees = orientation.degrees) {
                val announce = state.announce
                when {
                    showSettings -> SettingsScreen(
                        state = state,
                        strings = strings,
                        viewModel = viewModel,
                        onClose = { showSettings = false },
                    )
                    announce != null -> AnnouncementScreen(
                        slot = announce,
                        state = state,
                        strings = strings,
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
