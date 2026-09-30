package app.threedollars.common.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class TooltipTailDirection {
    TOP_END,
    BOTTOM_CENTER,
}

@Composable
fun Tooltip(
    emoji: String,
    message: String,
    tailDirection: TooltipTailDirection,
    modifier: Modifier = Modifier,
    tailEndPadding: Dp = 16.dp,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = if (tailDirection == TooltipTailDirection.TOP_END) Alignment.End else Alignment.CenterHorizontally,
    ) {
        if (tailDirection == TooltipTailDirection.TOP_END) {
            TooltipTail(pointingUp = true, modifier = Modifier.padding(end = tailEndPadding))
        }
        Row(
            modifier = Modifier
                .background(Gray90, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = emoji, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = message, fontSize = 14.sp, lineHeight = 20.sp, color = White)
        }
        if (tailDirection == TooltipTailDirection.BOTTOM_CENTER) {
            TooltipTail(pointingUp = false)
        }
    }
}

@Composable
private fun TooltipTail(pointingUp: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(width = 10.dp, height = 6.dp)) {
        val path = Path().apply {
            if (pointingUp) {
                moveTo(size.width / 2, 0f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
            } else {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width / 2, size.height)
            }
            close()
        }
        drawPath(path = path, color = Gray90)
    }
}
