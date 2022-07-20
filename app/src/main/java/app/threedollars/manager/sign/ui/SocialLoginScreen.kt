package app.threedollars.manager.sign.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.threedollars.manager.R
import app.threedollars.manager.sign.ui.component.SocialLoginButton

@Preview
@Composable
fun SocialLoginScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_back),
            contentDescription = "",
            modifier = Modifier
                .padding(top = 150.dp, bottom = 20.dp),
            contentScale = ContentScale.Crop
        )
        SocialLoginButton {

        }
    }
}