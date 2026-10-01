package app.threedollars.manager.feature.storemanagement.components.message

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.threedollars.common.analytics.ScreenViewLogEffect
import app.threedollars.common.ui.CircleProgressBar
import app.threedollars.common.ui.Gray0
import app.threedollars.common.ui.Gray40
import app.threedollars.common.ui.Gray5
import app.threedollars.common.ui.Gray60
import app.threedollars.common.ui.Gray90
import app.threedollars.common.ui.Gray95
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.LightGreen
import app.threedollars.common.ui.White
import app.threedollars.domain.dto.StoreMessageDto
import app.threedollars.manager.feature.storemanagement.R
import app.threedollars.manager.feature.storemanagement.StoreManagementLog
import app.threedollars.manager.feature.storemanagement.components.SystemAlertDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import java.time.LocalDateTime

private const val TOAST_DURATION_MILLIS = 3_000L
private const val COUNTDOWN_TICK_MILLIS = 1_000L

@Composable
internal fun MessageTab(storeId: String) {
    val viewModel: MessageViewModel = hiltViewModel()
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()
    var toastKey by remember { mutableIntStateOf(0) }

    ScreenViewLogEffect(screenName = StoreManagementLog.SCREEN_MESSAGE)

    LaunchedEffect(storeId) {
        if (storeId.isNotEmpty()) viewModel.onTabEntered(storeId)
    }
    LaunchedEffect(Unit) {
        viewModel.sentFlow.collect { toastKey += 1 }
    }

    MessageScreen(
        uiState = uiState,
        toastKey = toastKey,
        onRefresh = viewModel::refresh,
        onLoadMore = viewModel::loadNextPage,
        onSendClick = { viewModel.openCompose() },
        onComposeDismiss = viewModel::dismissCompose,
        onBodyChange = viewModel::updateBody,
        onInputFocusChange = viewModel::onInputFocusChanged,
        onInputTouch = viewModel::onInputTouched,
        onComposeSubmit = viewModel::submitCompose,
        onConfirmSend = viewModel::send,
        onConfirmRewrite = viewModel::rewrite,
        onConfirmDismiss = viewModel::dismissConfirm,
        onErrorDismiss = viewModel::clearError,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MessageScreen(
    uiState: MessageState,
    toastKey: Int,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onSendClick: () -> Unit,
    onComposeDismiss: () -> Unit,
    onBodyChange: (String) -> Unit,
    onInputFocusChange: (Boolean) -> Unit,
    onInputTouch: () -> Unit,
    onComposeSubmit: () -> Unit,
    onConfirmSend: () -> Unit,
    onConfirmRewrite: () -> Unit,
    onConfirmDismiss: () -> Unit,
    onErrorDismiss: () -> Unit,
) {
    uiState.compose?.let { compose ->
        MessageSendingBottomSheet(
            compose = compose,
            onDismiss = onComposeDismiss,
            onBodyChange = onBodyChange,
            onFocusChange = onInputFocusChange,
            onInputTouch = onInputTouch,
            onSubmit = onComposeSubmit,
        )
    }
    uiState.confirm?.let { confirm ->
        MessageConfirmDialog(
            storeName = uiState.storeName,
            body = confirm.body,
            onSend = onConfirmSend,
            onRewrite = onConfirmRewrite,
            onDismiss = onConfirmDismiss,
        )
    }
    uiState.error?.let { error ->
        SystemAlertDialog(
            message = error.message?.takeIf { it.isNotBlank() } ?: stringResource(R.string.message_request_failed),
            onConfirm = onErrorDismiss,
        )
    }

    var now by remember { mutableStateOf(LocalDateTime.now()) }
    val sendButtonState = uiState.sendButtonState(now)
    LaunchedEffect(uiState.policy, uiState.subscriberCount) {
        now = LocalDateTime.now()
        while (uiState.sendButtonState(now) is MessageSendButtonState.Countdown) {
            delay(COUNTDOWN_TICK_MILLIS)
            now = LocalDateTime.now()
        }
    }

    var showToast by remember { mutableStateOf(false) }
    LaunchedEffect(toastKey) {
        if (toastKey > 0) {
            showToast = true
            delay(TOAST_DURATION_MILLIS)
            showToast = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray0),
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize(),
        ) {
            if (uiState.isInitialLoaded) {
                MessageContentList(uiState = uiState, onLoadMore = onLoadMore)
            } else {
                Spacer(modifier = Modifier.fillMaxSize())
            }
        }
        if (uiState.isLoading && !uiState.isInitialLoaded) {
            CircleProgressBar(modifier = Modifier.align(Alignment.Center))
        }
        SendMessageButton(
            state = sendButtonState,
            onClick = onSendClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 24.dp),
        )
        AnimatedVisibility(
            visible = showToast,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 24.dp, end = 24.dp, bottom = 84.dp),
        ) {
            Text(
                text = stringResource(R.string.message_send_success_toast),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(Gray90, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            )
        }
    }
}

@Composable
private fun MessageContentList(uiState: MessageState, onLoadMore: () -> Unit) {
    val listState = rememberLazyListState()
    val lastMessageId = uiState.messages.lastOrNull()?.messageId
    val canLoadMore = uiState.canLoadMore

    LaunchedEffect(listState, lastMessageId, canLoadMore) {
        if (lastMessageId == null) return@LaunchedEffect
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.any { it.key == lastMessageId } }
            .distinctUntilChanged()
            .filter { isLastVisible -> isLastVisible }
            .collect { if (canLoadMore) onLoadMore() }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 92.dp),
    ) {
        when (uiState.screenType) {
            MessageScreenType.NO_BOOKMARK -> {
                item(key = "banner") { MessageDisableBanner() }
                item(key = "firstTitle") { MessageFirstTitle() }
                item(key = "bookmark") { MessageGuideImage(resId = R.drawable.img_message_bookmark) }
                item(key = "introduction") { MessageGuideImage(resId = R.drawable.img_message_introduction, topPadding = 12) }
            }

            MessageScreenType.NO_HISTORY -> {
                item(key = "overview") { MessageOverview(subscriberCount = uiState.subscriberCount) }
                item(key = "introduction") { MessageGuideImage(resId = R.drawable.img_message_introduction, topPadding = 12) }
            }

            MessageScreenType.HISTORY -> {
                item(key = "overview") { MessageOverview(subscriberCount = uiState.subscriberCount) }
                messageHistory(uiState = uiState)
            }
        }
    }
}

private fun LazyListScope.messageHistory(uiState: MessageState) {
    item(key = "historyHeader") {
        Text(
            text = stringResource(R.string.message_history_header),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Gray95,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .background(White, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 12.dp),
        )
    }
    val now = LocalDateTime.now()
    itemsIndexed(uiState.messages, key = { _, message -> message.messageId }) { _, message ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
        ) {
            MessageHistoryCell(message = message, now = now)
        }
    }
    item(key = "historyFooter") {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(vertical = 12.dp),
        ) {
            if (uiState.isLoadingMore) {
                CircleProgressBar(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun MessageHistoryCell(message: StoreMessageDto, now: LocalDateTime) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray5, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(text = message.body, fontSize = 14.sp, lineHeight = 20.sp, color = Gray95)
        Text(
            text = MessageFormatter.formatSentAt(message.createdAt, now),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Gray60,
        )
    }
}

@Composable
private fun MessageDisableBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 12.dp)
            .background(Gray90, RoundedCornerShape(12.dp))
            .height(44.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_information),
            contentDescription = null,
            tint = White,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = stringResource(R.string.message_disable_banner),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = White,
        )
    }
}

@Composable
private fun MessageFirstTitle() {
    Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 20.dp)) {
        FirstTitleText(text = AnnotatedString(stringResource(R.string.message_first_title_first)))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_bookmark_solid),
                contentDescription = null,
                tint = Green,
                modifier = Modifier.size(27.dp),
            )
            FirstTitleText(
                text = highlight(
                    text = stringResource(R.string.message_first_title_second),
                    target = stringResource(R.string.message_first_title_second_colored),
                    color = Green,
                ),
            )
        }
        FirstTitleText(
            text = highlight(
                text = stringResource(R.string.message_first_title_third),
                target = stringResource(R.string.message_first_title_third_colored),
                color = Green,
            ),
        )
    }
}

@Composable
private fun FirstTitleText(text: AnnotatedString) {
    Text(text = text, fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold, color = Gray95)
}

@Composable
private fun MessageGuideImage(resId: Int, topPadding: Int = 0) {
    Image(
        painter = painterResource(resId),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = topPadding.dp),
    )
}

@Composable
private fun MessageOverview(subscriberCount: Int) {
    Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.message_overview_user_count, subscriberCount),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Green,
                modifier = Modifier
                    .background(LightGreen, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
            Text(
                text = stringResource(R.string.message_overview_title_first),
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Gray95,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
        Text(
            text = stringResource(R.string.message_overview_title_second),
            fontSize = 24.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Gray95,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            OverviewDescription(text = AnnotatedString(stringResource(R.string.message_overview_description_first)))
            OverviewDescription(
                text = highlight(
                    text = stringResource(R.string.message_overview_description_second),
                    target = stringResource(R.string.message_overview_description_second_colored),
                    color = Green,
                ),
            )
            OverviewDescription(
                text = highlight(
                    text = stringResource(R.string.message_overview_description_third),
                    target = stringResource(R.string.message_overview_description_third_colored),
                    color = Green,
                ),
            )
        }
    }
}

@Composable
private fun OverviewDescription(text: AnnotatedString) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_information),
            contentDescription = null,
            tint = Green,
            modifier = Modifier.size(20.dp),
        )
        Text(text = text, fontSize = 14.sp, lineHeight = 20.sp, color = Gray60)
    }
}

@Composable
private fun SendMessageButton(
    state: MessageSendButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val enabled = state is MessageSendButtonState.Enabled
    val shape = RoundedCornerShape(22.dp)
    val title = when (state) {
        is MessageSendButtonState.Countdown -> stringResource(R.string.message_send_button_countdown, state.remaining)
        else -> stringResource(R.string.message_send_button)
    }
    Row(
        modifier = modifier
            .then(
                if (enabled) {
                    Modifier.shadow(6.dp, shape, ambientColor = Green.copy(alpha = 0.4f), spotColor = Green.copy(alpha = 0.4f))
                } else {
                    Modifier
                },
            )
            .background(if (enabled) Green else Gray40, shape)
            .clickable(enabled = enabled, onClick = onClick)
            .height(44.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_community_solid),
            contentDescription = null,
            tint = White,
            modifier = Modifier.size(20.dp),
        )
        Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = White)
    }
}

internal fun highlight(text: String, target: String, color: Color, fontWeight: FontWeight? = null): AnnotatedString =
    buildAnnotatedString {
        append(text)
        val start = text.indexOf(target)
        if (start >= 0) {
            addStyle(SpanStyle(color = color, fontWeight = fontWeight), start, start + target.length)
        }
    }
