package app.threedollars.manager.feature.storemanagement.components.storepost

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.threedollars.common.BaseDialog
import app.threedollars.common.analytics.AnalyticsLogger
import app.threedollars.common.analytics.ScreenViewLogEffect
import app.threedollars.common.ui.CircleProgressBar
import app.threedollars.common.ui.Gray0
import app.threedollars.common.ui.Gray95
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.White
import app.threedollars.domain.dto.StorePostDto
import app.threedollars.manager.feature.storemanagement.R
import app.threedollars.manager.feature.storemanagement.components.SystemAlertDialog
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
internal fun StorePostTab(
    storeId: String,
    onUploadNavigate: () -> Unit,
) {
    val viewModel: StorePostViewModel = hiltViewModel()
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ScreenViewLogEffect(screenName = "storePost")

    LaunchedEffect(storeId) {
        if (storeId.isNotEmpty()) {
            viewModel.onTabEntered(storeId)
        }
    }

    StorePostScreen(
        uiState = uiState,
        onLoadMore = viewModel::loadNextPage,
        onUploadClick = {
            AnalyticsLogger.logEvent(context, "clickUploadPost")
            viewModel.startUpload()
            onUploadNavigate()
        },
        onEditClick = { post ->
            viewModel.startUpload(post)
            onUploadNavigate()
        },
        onDeleteClick = { post -> viewModel.requestDelete(post.postId) },
        onDeleteCancel = viewModel::cancelDelete,
        onDeleteConfirm = viewModel::confirmDelete,
        onErrorDismiss = viewModel::clearError,
        onScrollToTopConsumed = viewModel::consumeScrollToTop,
    )
}

@Composable
internal fun StorePostScreen(
    uiState: StorePostState,
    onLoadMore: () -> Unit,
    onUploadClick: () -> Unit,
    onEditClick: (StorePostDto) -> Unit,
    onDeleteClick: (StorePostDto) -> Unit,
    onDeleteCancel: () -> Unit,
    onDeleteConfirm: () -> Unit,
    onErrorDismiss: () -> Unit,
    onScrollToTopConsumed: () -> Unit,
) {
    if (uiState.deleteTargetPostId != null) {
        SystemAlertDialog(
            message = "게시글을 삭제하시겠습니까?",
            confirmText = "삭제",
            dismissText = "취소",
            onConfirm = onDeleteConfirm,
            onDismiss = onDeleteCancel,
        )
    }
    if (uiState.errorMessage != null) {
        BaseDialog(
            title = "Error",
            message = uiState.errorMessage,
            confirmText = "확인",
            onConfirm = onErrorDismiss,
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray0),
    ) {
        when {
            uiState.isEmpty -> StorePostEmptyView()
            uiState.posts.isNotEmpty() -> StorePostList(
                uiState = uiState,
                onLoadMore = onLoadMore,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                onScrollToTopConsumed = onScrollToTopConsumed,
            )
        }
        if (uiState.isLoading) {
            CircleProgressBar(modifier = Modifier.align(Alignment.Center))
        }
        UploadPostButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 12.dp),
            onClick = onUploadClick,
        )
    }
}

@Composable
private fun StorePostList(
    uiState: StorePostState,
    onLoadMore: () -> Unit,
    onEditClick: (StorePostDto) -> Unit,
    onDeleteClick: (StorePostDto) -> Unit,
    onScrollToTopConsumed: () -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.scrollToTopRequested) {
        if (uiState.scrollToTopRequested) {
            listState.scrollToItem(0)
            onScrollToTopConsumed()
        }
    }

    LaunchedEffect(listState, uiState.posts.size, uiState.canLoadMore) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .filter { lastVisible -> lastVisible != null && lastVisible >= uiState.posts.lastIndex }
            .collect { if (uiState.canLoadMore) onLoadMore() }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(uiState.posts, key = { it.postId }) { post ->
            StorePostCard(post = post, onEditClick = onEditClick, onDeleteClick = onDeleteClick)
        }
        if (uiState.isLoadingMore) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                    CircleProgressBar(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
private fun StorePostEmptyView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.img_empty_post),
            contentDescription = null,
            modifier = Modifier.size(width = 208.dp, height = 108.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = buildAnnotatedString {
                append("다양한 우리 가게의\n")
                withStyle(SpanStyle(color = Green)) { append("새로운 소식") }
                append("을 알려보세요!")
            },
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Gray95,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun UploadPostButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(22.dp), ambientColor = Green, spotColor = Green)
            .background(Green, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .height(44.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_write_solid),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = "소식 올리기",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = White,
        )
    }
}
