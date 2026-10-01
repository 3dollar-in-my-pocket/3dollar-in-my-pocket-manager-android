package app.threedollars.manager.feature.storemanagement.components.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import app.threedollars.common.ui.Gray50
import app.threedollars.common.ui.Gray60
import app.threedollars.common.ui.Red
import app.threedollars.common.ui.White
import app.threedollars.manager.feature.storemanagement.R

@Composable
internal fun AccountDeleteDialog(
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        AccountDeleteDialogContent(onDelete = onDelete, onDismiss = onDismiss)
    }
}

@Composable
private fun AccountDeleteDialogContent(
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    val buttonShape = RoundedCornerShape(8.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(20.dp))
            .padding(start = 20.dp, top = 24.dp, end = 20.dp, bottom = 20.dp),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.account_delete_dialog_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Gray100,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.account_delete_dialog_message),
            fontSize = 14.sp,
            color = Gray60,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(buttonShape)
                .background(Red)
                .clickable(onClick = onDelete),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.account_delete_dialog_delete),
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
                .border(width = 1.dp, color = Gray50, shape = buttonShape)
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.account_delete_dialog_close),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Gray50,
            )
        }
    }
}

@Preview
@Composable
private fun AccountDeleteDialogContentPreview() {
    AccountDeleteDialogContent(onDelete = {}, onDismiss = {})
}
