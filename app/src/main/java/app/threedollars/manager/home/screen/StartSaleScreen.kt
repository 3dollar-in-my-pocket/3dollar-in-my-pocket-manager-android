package app.threedollars.manager.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Green400
import app.threedollars.common.ui.Green500
import app.threedollars.manager.R
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StartSaleScreen(
    modifier: Modifier = Modifier,
    startTime: Long,
    isSale: Boolean,
    startClick: () -> Unit
) {
    Column(
        modifier = modifier.background(color = Green500)
    ) {
        var saleTime by rememberSaveable { mutableStateOf("") }

        LaunchedEffect(key1 = isSale) {
            while (isSale) {
                val now = System.currentTimeMillis() - startTime
                saleTime = SimpleDateFormat("HH시간 mm분 ss초", Locale.getDefault()).format(Date(now))
                delay(1000L)
            }
        }
        Row(modifier = Modifier.padding(start = 26.dp, top = 24.dp)) {
            Text(
                text = "오늘은",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
            Text(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Green400),
                text = saleTime,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        }
        Text(
            modifier = Modifier.padding(start = 26.dp, top = 8.dp),
            text = "동안 영업중이시네요! 오늘도 대박나세요!",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
        )
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            onClick = startClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.shutter_down),
                fontSize = 16.sp,
                color = Green500
            )
        }
    }
}