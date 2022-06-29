package app.threedollars.manager.home.content

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.constraintlayout.compose.ConstraintLayout
import app.threedollars.common.ui.PinkOpacity20
import app.threedollars.manager.R
import kotlin.math.roundToInt

@Composable
fun CenterMarkerContent(isSale: Boolean) {
    var offsetX by rememberSaveable { mutableStateOf(0f) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }

    val modifier = if (isSale) Modifier.offset {
        IntOffset(
            (offsetX).roundToInt(),
            (offsetY).roundToInt()
        )
    } else {
        Modifier
            .offset {
                IntOffset(
                    (offsetX).roundToInt(),
                    (offsetY).roundToInt()
                )
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    }

    ConstraintLayout {
        val image = createRef()
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawCircle(
                color = PinkOpacity20,
                center = Offset(x = size.width / 2, y = size.height / 2),
                radius = size.minDimension / 5
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_my_gps),
            contentDescription = "myGps",
            modifier = modifier
                .constrainAs(image) {
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent)
            }
        )
    }
}
