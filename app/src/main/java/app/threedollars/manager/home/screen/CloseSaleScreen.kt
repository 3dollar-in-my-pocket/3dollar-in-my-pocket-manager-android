package app.threedollars.manager.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Black
import app.threedollars.common.ui.Green500
import app.threedollars.manager.R

@Composable
fun CloseSaleScreen(modifier: Modifier = Modifier, closeClick: () -> Unit) {
    Column(
        modifier = modifier.background(color = Color.White)
    ) {
        Row(modifier = Modifier.padding(start = 26.dp, top = 24.dp)) {
            Text(
                text = "지금 계신 위치에서 영업을 시작할까요?",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Black
            )
        }
        Text(
            modifier = Modifier.padding(start = 26.dp, top = 8.dp),
            text = "영업을 시작하면 위치가 손님들에게 공개됩니다.",
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Black
        )
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = Green500),
            onClick = closeClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(R.string.start_sale_today),
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}