package app.threedollars.manager.sign.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Green

@Composable
fun BasicButton(
    text: String,
    backgroundColor: Color = Green,
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp),
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = textColor,
        )
    }
}