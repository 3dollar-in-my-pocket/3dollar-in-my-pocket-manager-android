package app.threedollars.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import app.threedollars.common.ui.Gray70
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.White

@Composable
fun BaseDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String? = null,
    onConfirm: () -> Unit,
    onDismiss: (() -> Unit)? = null,
) {
    Row() {
        val annotatedMessage = buildAnnotatedString {
            withStyle(style = SpanStyle()) {
                append(message)
            }
        }
        AlertDialog(
            onDismissRequest = { onDismiss },
            title = { Text(modifier = Modifier.fillMaxWidth(), text = title, textAlign = TextAlign.Center) },
            text = { Text(modifier = Modifier.fillMaxWidth(), text = annotatedMessage, textAlign = TextAlign.Center) },
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (onDismiss != null) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(8.dp), onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(
                                contentColor = White,
                                backgroundColor = Gray70
                            )
                        ) {
                            if (dismissText != null) {
                                Text(text = dismissText)
                            }
                        }
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = White,
                            backgroundColor = Green
                        ),
                        onClick = onConfirm
                    ) {
                        Text(text = confirmText)
                    }
                }
            },
        )
    }
}