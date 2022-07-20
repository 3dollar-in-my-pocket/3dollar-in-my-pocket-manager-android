package app.threedollars.manager.sign.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Gray787878
import app.threedollars.common.ui.Green
import app.threedollars.manager.R

@Preview
@Composable
fun SignUpWaitingScreen() {
    Column(
        Modifier
            .background(Color.White)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 24.dp, vertical = 50.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.join_waiting_title),
                fontSize = 30.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(
                    id = R.string.join_waiting_message
                ),
                style = TextStyle(color = Gray787878)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Green),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.ask_email),
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    onClick = { },
                    contentPadding = PaddingValues(16.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.logout),
                        color = Color.Red,
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}