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
    AppTheme.DARK -> DarkPalette
    AppTheme.BLACK -> BlackPalette
    AppTheme.EMERALD -> EmeraldPalette
    AppTheme.MIDNIGHT -> MidnightPalette
    AppTheme.BURGUNDY -> BurgundyPalette
    AppTheme.LIGHT -> LightPalette
    AppTheme.GOLD -> GoldPalette
    AppTheme.BLUE -> BluePalette
    AppTheme.GREEN -> GreenPalette
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
    AppTheme.DARK -> DarkScheme
    AppTheme.BLACK -> BlackScheme
    AppTheme.EMERALD -> EmeraldScheme
    AppTheme.MIDNIGHT -> MidnightScheme
    AppTheme.BURGUNDY -> BurgundyScheme
    AppTheme.LIGHT -> LightScheme
    AppTheme.GOLD -> GoldScheme
    AppTheme.BLUE -> BlueScheme
    AppTheme.GREEN -> GreenScheme
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
fun PrimeMosqueTheme(theme: AppTheme = AppTheme.DARK, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalBoardPalette provides paletteFor(theme)) {
        MaterialTheme(
            colorScheme = schemeFor(theme),
            typography = Typography,
            content = content,
        )
    }
}
