package dev.mergim.primemosque.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.mergim.primemosque.data.AppTheme

/** Colors used directly by the prayer board and settings screens. */
data class BoardPalette(
    val bgTop: Color,
    val bgBottom: Color,
    val accent: Color,
    val card: Color,
    val cardHighlight: Color,
)

val DarkPalette = BoardPalette(
    bgTop = Navy,
    bgBottom = DeepNavy,
    accent = Gold,
    card = Navy.copy(alpha = 0.55f),
    cardHighlight = NavyLight,
)

val LightPalette = BoardPalette(
    bgTop = Color(0xFFFAF6EC),
    bgBottom = Color(0xFFEFE6D3),
    accent = Color(0xFF96731C),
    card = Color(0xB3FFFFFF),
    cardHighlight = Color(0xFFFFF4D6),
)

val LocalBoardPalette = staticCompositionLocalOf { DarkPalette }

private val DarkScheme = darkColorScheme(
    primary = Gold,
    onPrimary = GoldDark,
    secondary = Teal,
    onSecondary = DeepNavy,
    background = DeepNavy,
    onBackground = Cream,
    surface = CardNavy,
    onSurface = Cream,
    surfaceVariant = NavyLight,
    onSurfaceVariant = CreamMuted,
    outline = NavyLight,
)

private val LightScheme = lightColorScheme(
    primary = Color(0xFF96731C),
    onPrimary = Color.White,
    secondary = Color(0xFF0C6E60),
    onSecondary = Color.White,
    background = Color(0xFFFAF6EC),
    onBackground = Color(0xFF20303F),
    surface = Color(0xFFFDFAF2),
    onSurface = Color(0xFF20303F),
    surfaceVariant = Color(0xFFEDE4CF),
    onSurfaceVariant = Color(0xFF5D6C7B),
    outline = Color(0xFFC9BC9E),
)

@Composable
fun PrimeMosqueTheme(theme: AppTheme = AppTheme.DARK, content: @Composable () -> Unit) {
    val palette = if (theme == AppTheme.LIGHT) LightPalette else DarkPalette
    CompositionLocalProvider(LocalBoardPalette provides palette) {
        MaterialTheme(
            colorScheme = if (theme == AppTheme.LIGHT) LightScheme else DarkScheme,
            typography = Typography,
            content = content,
        )
    }
}
