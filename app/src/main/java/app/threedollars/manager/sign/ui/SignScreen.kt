package app.threedollars.manager.sign.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SignScreen() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Sign")
    }

}