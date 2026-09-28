package app.threedollars.manager.feature.storemanagement.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Gray60
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.White

@Composable
internal fun SystemAlertDialog(
    message: String,
    confirmText: String = "확인",
    dismissText: String? = null,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = onConfirm,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,
        text = { Text(text = message, fontSize = 16.sp, color = Gray100) },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(text = confirmText, color = Green) }
        },
        dismissButton = dismissText?.let { text ->
            { TextButton(onClick = onDismiss) { Text(text = text, color = Gray60) } }
        },
    )
}
