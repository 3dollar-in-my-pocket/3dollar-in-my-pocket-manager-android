package app.threedollars.manager

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginButtons()
        }
    }
}

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
        Row() {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "카카오 계정으로 로그인")
            }
        }
        Row() {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "네이버 계정으로 로그인")
            }
        }
    }
}