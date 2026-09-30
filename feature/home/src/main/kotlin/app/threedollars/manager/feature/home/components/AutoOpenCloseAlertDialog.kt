package app.threedollars.manager.feature.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import app.threedollars.common.ui.Gray100
import androidx.compose.ui.res.colorResource
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.White
import app.threedollars.manager.feature.home.R

@Composable
internal fun AutoOpenCloseAlertDialog(
    onPreferenceClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        AutoOpenCloseAlertContent(
            onPreferenceClick = onPreferenceClick,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun AutoOpenCloseAlertContent(
    onPreferenceClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    val buttonShape = RoundedCornerShape(8.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(id = R.string.home_auto_alert_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Gray100,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.home_auto_alert_description),
            fontSize = 14.sp,
            color = colorResource(id = R.color.gray50),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(buttonShape)
                .background(Green)
                .clickable(onClick = onPreferenceClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.home_auto_alert_go_preference),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = White,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(buttonShape)
                .background(White)
                .border(BorderStroke(1.dp, Green), buttonShape)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.home_auto_alert_close),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Green,
            )
        }
    }
}

@Preview
@Composable
private fun AutoOpenCloseAlertContentPreview() {
    AutoOpenCloseAlertContent(onPreferenceClick = {}, onDismiss = {})
}
