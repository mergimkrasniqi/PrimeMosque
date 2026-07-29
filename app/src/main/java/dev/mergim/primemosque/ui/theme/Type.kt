package dev.mergim.primemosque.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import dev.mergim.primemosque.R

/** UI font for titles, labels and body text (variable weight). */
val SpaceGrotesk = FontFamily(
    Font(R.font.space_grotesk_light, weight = androidx.compose.ui.text.font.FontWeight.Light),
    Font(R.font.space_grotesk_regular, weight = androidx.compose.ui.text.font.FontWeight.Normal),
    Font(R.font.space_grotesk_medium, weight = androidx.compose.ui.text.font.FontWeight.Medium),
    Font(R.font.space_grotesk_bold, weight = androidx.compose.ui.text.font.FontWeight.Bold),
)

private val base = Typography()

/** Material typography with Space Grotesk as the default face everywhere. */
val Typography = Typography(
    displayLarge = base.displayLarge.copy(fontFamily = SpaceGrotesk),
    displayMedium = base.displayMedium.copy(fontFamily = SpaceGrotesk),
    displaySmall = base.displaySmall.copy(fontFamily = SpaceGrotesk),
    headlineLarge = base.headlineLarge.copy(fontFamily = SpaceGrotesk),
    headlineMedium = base.headlineMedium.copy(fontFamily = SpaceGrotesk),
    headlineSmall = base.headlineSmall.copy(fontFamily = SpaceGrotesk),
    titleLarge = base.titleLarge.copy(fontFamily = SpaceGrotesk),
    titleMedium = base.titleMedium.copy(fontFamily = SpaceGrotesk),
    titleSmall = base.titleSmall.copy(fontFamily = SpaceGrotesk),
    bodyLarge = base.bodyLarge.copy(fontFamily = SpaceGrotesk),
    bodyMedium = base.bodyMedium.copy(fontFamily = SpaceGrotesk),
    bodySmall = base.bodySmall.copy(fontFamily = SpaceGrotesk),
    labelLarge = base.labelLarge.copy(fontFamily = SpaceGrotesk),
    labelMedium = base.labelMedium.copy(fontFamily = SpaceGrotesk),
    labelSmall = base.labelSmall.copy(fontFamily = SpaceGrotesk),
)
