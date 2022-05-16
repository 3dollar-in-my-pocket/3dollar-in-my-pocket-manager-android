package app.threedollars.manager

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.threedollars.manager.sign.ui.LoginButtons
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SocialLoginScreen()
        }
    }
}

@Preview
@Composable
fun SocialLoginScreen() {
    Column {
        LoginButtons()
    }
}
