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

/** Warm mushaf cream with deep red accents (flat background). */
val MushafPalette = BoardPalette(
    bgTop = Color(0xFFFFF0DB),
    bgBottom = Color(0xFFFFF0DB),
    accent = Color(0xFF9C1717),
    card = Color(0xFFF6E5CD),
    cardHighlight = Color(0xFFF0DBBB),
)

/** Mushaf at night: dark leather brown with burnt-orange accents. */
val MushafDarkPalette = BoardPalette(
    bgTop = Color(0xFF17120D),
    bgBottom = Color(0xFF17120D),
    accent = Color(0xFFC9502F),
    card = Color(0xFF292019),
    cardHighlight = Color(0xFF332822),
)

/** Olive parchment with deep olive-green accents. */
val ZaytunPalette = BoardPalette(
    bgTop = Color(0xFFF0EDDD),
    bgBottom = Color(0xFFF0EDDD),
    accent = Color(0xFF4F5533),
    card = Color(0xFFE4E0CB),
    cardHighlight = Color(0xFFDBD6BB),
)

/** Olive at night: near-black moss with soft sage accents. */
val ZaytunDarkPalette = BoardPalette(
    bgTop = Color(0xFF14150F),
    bgBottom = Color(0xFF14150F),
    accent = Color(0xFFA8AC82),
    card = Color(0xFF22221A),
    cardHighlight = Color(0xFF2C2C22),
)

/** Cool porcelain grey with indigo-slate accents. */
val NilaPalette = BoardPalette(
    bgTop = Color(0xFFEAECEF),
    bgBottom = Color(0xFFEAECEF),
    accent = Color(0xFF3D4A63),
    card = Color(0xFFDCDFE5),
    cardHighlight = Color(0xFFD0D4DC),
)

/** Indigo at night: charcoal blue with dusty steel accents. */
val NilaDarkPalette = BoardPalette(
    bgTop = Color(0xFF101216),
    bgBottom = Color(0xFF101216),
    accent = Color(0xFF97A3BA),
    card = Color(0xFF1D2026),
    cardHighlight = Color(0xFF262A31),
)

/** Paper and ink: warm off-white with near-black accents. */
val HibrPalette = BoardPalette(
    bgTop = Color(0xFFF2EFE8),
    bgBottom = Color(0xFFF2EFE8),
    accent = Color(0xFF33302B),
    card = Color(0xFFE5E1D7),
    cardHighlight = Color(0xFFDBD6C8),
)

/** Ink at night: true black-brown with bone-white accents. */
val HibrDarkPalette = BoardPalette(
    bgTop = Color(0xFF111110),
    bgBottom = Color(0xFF111110),
    accent = Color(0xFFD6CFC2),
    card = Color(0xFF1E1D1A),
    cardHighlight = Color(0xFF282723),
)

val LocalBoardPalette = staticCompositionLocalOf { MushafPalette }

private val MushafScheme = lightColorScheme(
    primary = Color(0xFF9C1717),
    onPrimary = Color(0xFFFFF0DB),
    secondary = Color(0xFF62594D),
    onSecondary = Color(0xFFFFF0DB),
    background = Color(0xFFFFF0DB),
    onBackground = Color(0xFF33302C),
    surface = Color(0xFFFFF0DB),
    onSurface = Color(0xFF33302C),
    surfaceVariant = Color(0xFFF6E5CD),
    onSurfaceVariant = Color(0xFF62594D),
    outline = Color(0xFF968977),
)

private val MushafDarkScheme = darkColorScheme(
    primary = Color(0xFFC9502F),
    onPrimary = Color(0xFF17120D),
    secondary = Color(0xFFA5967F),
    onSecondary = Color(0xFF17120D),
    background = Color(0xFF17120D),
    onBackground = Color(0xFFEADFCB),
    surface = Color(0xFF17120D),
    onSurface = Color(0xFFEADFCB),
    surfaceVariant = Color(0xFF292019),
    onSurfaceVariant = Color(0xFFA5967F),
    outline = Color(0xFF6F6353),
)

private val ZaytunScheme = lightColorScheme(
    primary = Color(0xFF4F5533),
    onPrimary = Color(0xFFF0EDDD),
    secondary = Color(0xFF5B5946),
    onSecondary = Color(0xFFF0EDDD),
    background = Color(0xFFF0EDDD),
    onBackground = Color(0xFF2D2C23),
    surface = Color(0xFFF0EDDD),
    onSurface = Color(0xFF2D2C23),
    surfaceVariant = Color(0xFFE4E0CB),
    onSurfaceVariant = Color(0xFF5B5946),
    outline = Color(0xFF918D77),
)

private val ZaytunDarkScheme = darkColorScheme(
    primary = Color(0xFFA8AC82),
    onPrimary = Color(0xFF14150F),
    secondary = Color(0xFF9A9781),
    onSecondary = Color(0xFF14150F),
    background = Color(0xFF14150F),
    onBackground = Color(0xFFE0DDCA),
    surface = Color(0xFF14150F),
    onSurface = Color(0xFFE0DDCA),
    surfaceVariant = Color(0xFF22221A),
    onSurfaceVariant = Color(0xFF9A9781),
    outline = Color(0xFF676453),
)

private val NilaScheme = lightColorScheme(
    primary = Color(0xFF3D4A63),
    onPrimary = Color(0xFFEAECEF),
    secondary = Color(0xFF51555E),
    onSecondary = Color(0xFFEAECEF),
    background = Color(0xFFEAECEF),
    onBackground = Color(0xFF24272D),
    surface = Color(0xFFEAECEF),
    onSurface = Color(0xFF24272D),
    surfaceVariant = Color(0xFFDCDFE5),
    onSurfaceVariant = Color(0xFF51555E),
    outline = Color(0xFF868B95),
)

private val NilaDarkScheme = darkColorScheme(
    primary = Color(0xFF97A3BA),
    onPrimary = Color(0xFF101216),
    secondary = Color(0xFF8F939C),
    onSecondary = Color(0xFF101216),
    background = Color(0xFF101216),
    onBackground = Color(0xFFDCDEE4),
    surface = Color(0xFF101216),
    onSurface = Color(0xFFDCDEE4),
    surfaceVariant = Color(0xFF1D2026),
    onSurfaceVariant = Color(0xFF8F939C),
    outline = Color(0xFF5F636C),
)

private val HibrScheme = lightColorScheme(
    primary = Color(0xFF33302B),
    onPrimary = Color(0xFFF2EFE8),
    secondary = Color(0xFF56534C),
    onSecondary = Color(0xFFF2EFE8),
    background = Color(0xFFF2EFE8),
    onBackground = Color(0xFF191817),
    surface = Color(0xFFF2EFE8),
    onSurface = Color(0xFF191817),
    surfaceVariant = Color(0xFFE5E1D7),
    onSurfaceVariant = Color(0xFF56534C),
    outline = Color(0xFF8B877E),
)

private val HibrDarkScheme = darkColorScheme(
    primary = Color(0xFFD6CFC2),
    onPrimary = Color(0xFF111110),
    secondary = Color(0xFF9B968C),
    onSecondary = Color(0xFF111110),
    background = Color(0xFF111110),
    onBackground = Color(0xFFE8E4DB),
    surface = Color(0xFF111110),
    onSurface = Color(0xFFE8E4DB),
    surfaceVariant = Color(0xFF1E1D1A),
    onSurfaceVariant = Color(0xFF9B968C),
    outline = Color(0xFF66625B),
)

private fun paletteFor(theme: AppTheme): BoardPalette = when (theme) {
    AppTheme.MUSHAF -> MushafPalette
    AppTheme.MUSHAF_DARK -> MushafDarkPalette
    AppTheme.ZAYTUN -> ZaytunPalette
    AppTheme.ZAYTUN_DARK -> ZaytunDarkPalette
    AppTheme.NILA -> NilaPalette
    AppTheme.NILA_DARK -> NilaDarkPalette
    AppTheme.HIBR -> HibrPalette
    AppTheme.HIBR_DARK -> HibrDarkPalette
}

private fun schemeFor(theme: AppTheme): ColorScheme = when (theme) {
    AppTheme.MUSHAF -> MushafScheme
    AppTheme.MUSHAF_DARK -> MushafDarkScheme
    AppTheme.ZAYTUN -> ZaytunScheme
    AppTheme.ZAYTUN_DARK -> ZaytunDarkScheme
    AppTheme.NILA -> NilaScheme
    AppTheme.NILA_DARK -> NilaDarkScheme
    AppTheme.HIBR -> HibrScheme
    AppTheme.HIBR_DARK -> HibrDarkScheme
}

@Composable
fun PrimeMosqueTheme(theme: AppTheme = AppTheme.MUSHAF, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalBoardPalette provides paletteFor(theme)) {
        MaterialTheme(
            colorScheme = schemeFor(theme),
            typography = Typography,
            content = content,
        )
    }
}
