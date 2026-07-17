package dev.mergim.primemosque.ui.theme

import androidx.compose.material3.ColorScheme
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

/** Elegant white/black/gold, inspired by classic Umrah poster design. */
val GoldPalette = BoardPalette(
    bgTop = Color(0xFFFCFBF7),
    bgBottom = Color(0xFFF0EBDD),
    accent = Color(0xFFB08A2A),
    card = Color(0xFFFFFFFF),
    cardHighlight = Color(0xFFF7EFD9),
)

/** Vivid masjid-board blue with white text. */
val BluePalette = BoardPalette(
    bgTop = Color(0xFF1E78C8),
    bgBottom = Color(0xFF0D4C8F),
    accent = Color(0xFFFFFFFF),
    card = Color(0x220A3D75),
    cardHighlight = Color(0x400A3D75),
)

/** Pure black with rich gold — elegant, and easy on OLED/LED panels at night. */
val BlackPalette = BoardPalette(
    bgTop = Color(0xFF141414),
    bgBottom = Color(0xFF000000),
    accent = Color(0xFFD4AF37),
    card = Color(0x14FFFFFF),
    cardHighlight = Color(0xFF201B0E),
)

/** Deep mosque green with soft gold accents. */
val EmeraldPalette = BoardPalette(
    bgTop = Color(0xFF0F3D2F),
    bgBottom = Color(0xFF06251C),
    accent = Color(0xFFE4C165),
    card = Color(0x14FFFFFF),
    cardHighlight = Color(0xFF14503F),
)

/** Cool slate blue with ice-blue accents instead of gold. */
val MidnightPalette = BoardPalette(
    bgTop = Color(0xFF1C2E42),
    bgBottom = Color(0xFF0E1C2C),
    accent = Color(0xFF7FB8E8),
    card = Color(0x14FFFFFF),
    cardHighlight = Color(0xFF223A54),
)

/** Deep burgundy, like mosque carpets, with warm gold accents. */
val BurgundyPalette = BoardPalette(
    bgTop = Color(0xFF451A24),
    bgBottom = Color(0xFF290D14),
    accent = Color(0xFFE0B96B),
    card = Color(0x14FFFFFF),
    cardHighlight = Color(0xFF5A2733),
)

/** Soft sage green with deep teal accents. */
val GreenPalette = BoardPalette(
    bgTop = Color(0xFFEDF1ED),
    bgBottom = Color(0xFFDCE5DE),
    accent = Color(0xFF43706B),
    card = Color(0xCCFFFFFF),
    cardHighlight = Color(0xFFD7E5DF),
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

private val BlackScheme = darkColorScheme(
    primary = Color(0xFFD4AF37),
    onPrimary = Color(0xFF1B1400),
    secondary = Color(0xFFD4AF37),
    onSecondary = Color(0xFF1B1400),
    background = Color(0xFF000000),
    onBackground = Color(0xFFF2EFE6),
    surface = Color(0xFF161616),
    onSurface = Color(0xFFF2EFE6),
    surfaceVariant = Color(0xFF242424),
    onSurfaceVariant = Color(0xFF9E978A),
    outline = Color(0xFF3A362B),
)

private val EmeraldScheme = darkColorScheme(
    primary = Color(0xFFE4C165),
    onPrimary = Color(0xFF231A00),
    secondary = Color(0xFF7FD1B9),
    onSecondary = Color(0xFF06251C),
    background = Color(0xFF06251C),
    onBackground = Color(0xFFF0F5EF),
    surface = Color(0xFF0E362A),
    onSurface = Color(0xFFF0F5EF),
    surfaceVariant = Color(0xFF14503F),
    onSurfaceVariant = Color(0xFFA8C4B8),
    outline = Color(0xFF2C6450),
)

private val MidnightScheme = darkColorScheme(
    primary = Color(0xFF7FB8E8),
    onPrimary = Color(0xFF0A1A29),
    secondary = Color(0xFF9CCEF5),
    onSecondary = Color(0xFF0A1A29),
    background = Color(0xFF0E1C2C),
    onBackground = Color(0xFFEAF2FA),
    surface = Color(0xFF182A3E),
    onSurface = Color(0xFFEAF2FA),
    surfaceVariant = Color(0xFF223A54),
    onSurfaceVariant = Color(0xFF97ABC1),
    outline = Color(0xFF3A567A),
)

private val BurgundyScheme = darkColorScheme(
    primary = Color(0xFFE0B96B),
    onPrimary = Color(0xFF231600),
    secondary = Color(0xFFE8A88F),
    onSecondary = Color(0xFF290D14),
    background = Color(0xFF290D14),
    onBackground = Color(0xFFF5EDE8),
    surface = Color(0xFF3A141D),
    onSurface = Color(0xFFF5EDE8),
    surfaceVariant = Color(0xFF5A2733),
    onSurfaceVariant = Color(0xFFC4A5A5),
    outline = Color(0xFF7A3B4A),
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

private val GoldScheme = lightColorScheme(
    primary = Color(0xFFB08A2A),
    onPrimary = Color.White,
    secondary = Color(0xFF8A6D1F),
    onSecondary = Color.White,
    background = Color(0xFFFCFBF7),
    onBackground = Color(0xFF17150F),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF17150F),
    surfaceVariant = Color(0xFFF0EBDD),
    onSurfaceVariant = Color(0xFF6E6857),
    outline = Color(0xFFD8CDAF),
)

private val BlueScheme = darkColorScheme(
    primary = Color.White,
    onPrimary = Color(0xFF0D4C8F),
    secondary = Color(0xFFFFE082),
    onSecondary = Color(0xFF0D4C8F),
    background = Color(0xFF0D4C8F),
    onBackground = Color.White,
    surface = Color(0xFF11569E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF1B62AC),
    onSurfaceVariant = Color(0xFFC7E1F9),
    outline = Color(0xFF5E97CF),
)

private val GreenScheme = lightColorScheme(
    primary = Color(0xFF43706B),
    onPrimary = Color.White,
    secondary = Color(0xFF43706B),
    onSecondary = Color.White,
    background = Color(0xFFEDF1ED),
    onBackground = Color(0xFF263D3A),
    surface = Color(0xFFF6F8F6),
    onSurface = Color(0xFF263D3A),
    surfaceVariant = Color(0xFFD7E5DF),
    onSurfaceVariant = Color(0xFF5C7370),
    outline = Color(0xFFA8C0B8),
)

private fun paletteFor(theme: AppTheme): BoardPalette = when (theme) {
    AppTheme.DARK -> DarkPalette
    AppTheme.BLACK -> BlackPalette
    AppTheme.EMERALD -> EmeraldPalette
    AppTheme.MIDNIGHT -> MidnightPalette
    AppTheme.BURGUNDY -> BurgundyPalette
    AppTheme.LIGHT -> LightPalette
    AppTheme.GOLD -> GoldPalette
    AppTheme.BLUE -> BluePalette
    AppTheme.GREEN -> GreenPalette
}

private fun schemeFor(theme: AppTheme): ColorScheme = when (theme) {
    AppTheme.DARK -> DarkScheme
    AppTheme.BLACK -> BlackScheme
    AppTheme.EMERALD -> EmeraldScheme
    AppTheme.MIDNIGHT -> MidnightScheme
    AppTheme.BURGUNDY -> BurgundyScheme
    AppTheme.LIGHT -> LightScheme
    AppTheme.GOLD -> GoldScheme
    AppTheme.BLUE -> BlueScheme
    AppTheme.GREEN -> GreenScheme
}

@Composable
fun PrimeMosqueTheme(theme: AppTheme = AppTheme.DARK, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalBoardPalette provides paletteFor(theme)) {
        MaterialTheme(
            colorScheme = schemeFor(theme),
            typography = Typography,
            content = content,
        )
    }
}
