package app.threedollars.manager.sign.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.threedollars.common.ui.Yellow
import app.threedollars.manager.R

@Composable
fun LoginButtons() {
    Column(
        modifier = Modifier.fillMaxSize(),
        Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.padding(bottom = 30.dp)) {
            Text(
                text = "우리 앱의 대표 그래픽",
                style = TextStyle(color = Color.Black)
            )
        }
        Row {
            SocialLoginButton {}
        }
    }
}

@Composable
fun SocialLoginButton(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .fillMaxWidth(),
            onClick = { onClick() },
            shape = RoundedCornerShape(23.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                backgroundColor = Yellow
            )
        ) {
            Text(
                text = stringResource(id = R.string.kakao_login),
                fontWeight = FontWeight.Bold
            )
        }
    }
}