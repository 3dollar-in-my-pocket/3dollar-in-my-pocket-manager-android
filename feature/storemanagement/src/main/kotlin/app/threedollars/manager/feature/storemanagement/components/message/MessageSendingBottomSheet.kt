package app.threedollars.manager.feature.storemanagement.components.message

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.analytics.ScreenViewLogEffect
import app.threedollars.common.ui.Gray10
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Gray40
import app.threedollars.common.ui.Gray50
import app.threedollars.common.ui.Gray95
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.Red
import app.threedollars.common.ui.White
import app.threedollars.manager.feature.storemanagement.R
import app.threedollars.manager.feature.storemanagement.StoreManagementLog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessageSendingBottomSheet(
    compose: MessageComposeState,
    onDismiss: () -> Unit,
    onBodyChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onInputTouch: () -> Unit,
    onSubmit: () -> Unit,
) {
    ScreenViewLogEffect(screenName = StoreManagementLog.SCREEN_MESSAGE_SENDING_BOTTOM_SHEET)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = White,
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding()
                .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 20.dp),
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = stringResource(R.string.message_sending_title),
                    fontSize = 20.sp,
                    lineHeight = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Gray100,
                    modifier = Modifier.weight(1f),
                )
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_close_24),
                    contentDescription = stringResource(R.string.message_sending_close),
                    modifier = Modifier
                        .size(30.dp)
                        .clickable(onClick = onDismiss)
                        .padding(3.dp),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = highlight(
                    text = stringResource(R.string.message_sending_description),
                    target = stringResource(R.string.message_sending_description_colored),
                    color = Gray95,
                ),
                fontSize = 14.sp,
                color = Gray50,
            )
            Spacer(modifier = Modifier.height(16.dp))
            MessageInputField(
                compose = compose,
                onBodyChange = onBodyChange,
                onFocusChange = onFocusChange,
                onInputTouch = onInputTouch,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.message_sending_warning),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (compose.inputState == MessageInputState.ERROR) Red else Gray50,
            )
            Spacer(modifier = Modifier.height(28.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Green, RoundedCornerShape(12.dp))
                    .clickable(onClick = onSubmit),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.message_sending_submit),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = White,
                )
            }
        }
    }
}

@Composable
private fun MessageInputField(
    compose: MessageComposeState,
    onBodyChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onInputTouch: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) onInputTouch()
        }
    }
    val borderColor = when (compose.inputState) {
        MessageInputState.NORMAL -> Color.Transparent
        MessageInputState.FOCUSED -> Green
        MessageInputState.ERROR -> Red
    }
    val counterColor = when (compose.inputState) {
        MessageInputState.NORMAL -> Gray50
        MessageInputState.FOCUSED -> Green
        MessageInputState.ERROR -> Red
    }
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .background(Gray10, shape)
            .border(width = 1.dp, color = borderColor, shape = shape)
            .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
    ) {
        BasicTextField(
            value = compose.body,
            onValueChange = onBodyChange,
            interactionSource = interactionSource,
            textStyle = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, color = Gray95),
            cursorBrush = SolidColor(Green),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .onFocusChanged { onFocusChange(it.isFocused) },
            decorationBox = { innerTextField ->
                Box {
                    if (compose.body.isEmpty()) {
                        Text(
                            text = stringResource(R.string.message_sending_placeholder),
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = Gray40,
                        )
                    }
                    innerTextField()
                }
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        val counterLimit = stringResource(R.string.message_sending_counter_limit, MESSAGE_MAX_LENGTH)
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = counterColor)) { append(compose.body.length.toString()) }
                append(counterLimit)
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Gray50,
        )
    }
}
