package dev.mergim.primemosque.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints

/**
 * Renders [content] rotated by [degrees] (0, 90, 180, 270).
 *
 * TV panels always output landscape; when the TV is physically mounted in
 * portrait, the UI itself must be rotated. For 90/270 the content is measured
 * with swapped width/height and drawn rotated around its center, so it fills
 * the physical portrait screen exactly.
 */
@Composable
fun RotatedLayout(
    degrees: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    if (degrees % 360 == 0) {
        content()
        return
    }
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val swap = degrees % 180 != 0
        val childConstraints = if (swap) {
            Constraints.fixed(constraints.maxHeight, constraints.maxWidth)
        } else {
            Constraints.fixed(constraints.maxWidth, constraints.maxHeight)
        }
        val placeables = measurables.map { it.measure(childConstraints) }
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { placeable ->
                placeable.placeWithLayer(
                    x = (constraints.maxWidth - placeable.width) / 2,
                    y = (constraints.maxHeight - placeable.height) / 2,
                ) {
                    rotationZ = degrees.toFloat()
                }
            }
        }
    }
}
