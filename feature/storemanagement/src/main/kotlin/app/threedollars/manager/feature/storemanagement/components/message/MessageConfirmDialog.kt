package app.threedollars.manager.feature.storemanagement.components.message

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.threedollars.common.analytics.ScreenViewLogEffect
import app.threedollars.common.ui.Gray10
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Gray95
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.White
import app.threedollars.manager.feature.storemanagement.R
import app.threedollars.manager.feature.storemanagement.StoreManagementLog

@Composable
internal fun MessageConfirmDialog(
    storeName: String,
    body: String,
    onSend: () -> Unit,
    onRewrite: () -> Unit,
    onDismiss: () -> Unit,
) {
    ScreenViewLogEffect(screenName = StoreManagementLog.SCREEN_MESSAGE_CONFIRM_DIALOG)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = false, usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(White, RoundedCornerShape(20.dp))
                .padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            Text(
                text = stringResource(R.string.message_confirm_title),
                fontSize = 20.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = Gray100,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Gray10, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = stringResource(R.string.message_confirm_preview_emoji), fontSize = 14.sp, lineHeight = 20.sp)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.message_confirm_preview_title, storeName),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Gray95,
                    )
                    Text(text = body, fontSize = 14.sp, lineHeight = 20.sp, color = Gray95)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            ConfirmButton(
                text = stringResource(R.string.message_confirm_send),
                containerColor = Green,
                contentColor = White,
                onClick = onSend,
            )
            Spacer(modifier = Modifier.height(8.dp))
            ConfirmButton(
                text = stringResource(R.string.message_confirm_rewrite),
                containerColor = White,
                contentColor = Green,
                borderColor = Green,
                onClick = onRewrite,
            )
        }
    }
}

@Composable
private fun ConfirmButton(
    text: String,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    borderColor: Color = Color.Transparent,
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(containerColor, shape)
            .border(1.dp, borderColor, shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = contentColor)
    }
}
