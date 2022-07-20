package app.threedollars.manager.sign.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.LightGreen
import java.lang.Integer.max

@Composable
fun CategoryTextButtonGroup(tags: List<Category>) {
    val tagList by remember {
        mutableStateOf(tags.toMutableList())
    }

    SimpleFlowRow(
        alignment = Alignment.Start,
        horizontalGap = 12.dp,
        verticalGap = 11.dp
    ) {
        for (tag in tagList) {
            var color = remember {
                if (tag.isSelected) Green else LightGreen
            }

            Text(
                text = tag.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .background(
                        color = color,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = null
                    ) {
                        tagList[tag.id] = tag.copy(isSelected = !tag.isSelected)
                        color = (if (!tag.isSelected) Green else LightGreen)
                    },
                color = if (tag.isSelected) Color.White else Green,
            )
        }
    }
}

@Composable
fun SimpleFlowRow(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    verticalGap: Dp = 0.dp,
    horizontalGap: Dp = 0.dp,
    content: @Composable () -> Unit
) = Layout(content, modifier) { measurables, constraints ->
    val hGapPx = horizontalGap.roundToPx()
    val vGapPx = verticalGap.roundToPx()

    val rows = mutableListOf<MeasuredRow>()
    val itemConstraints = constraints.copy(minWidth = 0)

    for (measurable in measurables) {
        val lastRow = rows.lastOrNull()
        val placeable = measurable.measure(itemConstraints)

        if (lastRow != null && lastRow.width + hGapPx + placeable.width <= constraints.maxWidth) {
            lastRow.items.add(placeable)
            lastRow.width += hGapPx + placeable.width
            lastRow.height = max(lastRow.height, placeable.height)
        } else {
            val nextRow = MeasuredRow(
                items = mutableListOf(placeable),
                width = placeable.width,
                height = placeable.height
            )

            rows.add(nextRow)
        }
    }

    val width = rows.maxOfOrNull { row -> row.width } ?: 0
    val height = rows.sumBy { row -> row.height } + max(vGapPx.times(rows.size - 1), 0)

    val coercedWidth = width.coerceIn(constraints.minWidth, constraints.maxWidth)
    val coercedHeight = height.coerceIn(constraints.minHeight, constraints.maxHeight)

    layout(coercedWidth, coercedHeight) {
        var y = 0

        for (row in rows) {
            var x = when (alignment) {
                Alignment.Start -> 0
                Alignment.CenterHorizontally -> (coercedWidth - row.width) / 2
                Alignment.End -> coercedWidth - row.width

                else -> throw Exception("unsupported alignment")
            }

            for (item in row.items) {
                item.place(x, y)
                x += item.width + hGapPx
            }

            y += row.height + vGapPx
        }
    }
}

private data class MeasuredRow(
    val items: MutableList<Placeable>,
    var width: Int,
    var height: Int
)

data class Category(
    val id: Int,
    val isSelected: Boolean,
    val name: String,
    val categoryId: String
)