package app.threedollars.common.ui.photo

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.threedollars.common.R
import app.threedollars.common.ui.Gray10
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Gray95
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.White
import kotlinx.coroutines.launch

/**
 * 사진 전체 화면 뷰어. 이미지 로딩은 호출하는 feature 가 [imageContent] 로 그린다.
 */
@Composable
fun PhotoViewerDialog(
    imageUrls: List<String>,
    initialIndex: Int,
    onDismiss: () -> Unit,
    imageContent: @Composable (imageUrl: String, modifier: Modifier) -> Unit,
) {
    if (imageUrls.isEmpty()) return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        )
    ) {
        PhotoViewerContent(
            imageUrls = imageUrls,
            initialIndex = initialIndex,
            onDismiss = onDismiss,
            imageContent = imageContent,
        )
    }
}

@Composable
private fun PhotoViewerContent(
    imageUrls: List<String>,
    initialIndex: Int,
    onDismiss: () -> Unit,
    imageContent: @Composable (imageUrl: String, modifier: Modifier) -> Unit,
) {
    val initialPosition = remember(initialIndex, imageUrls.size) { PhotoViewerPosition.of(initialIndex, imageUrls.size) }
    val pagerState = rememberPagerState(initialPage = initialPosition.index) { imageUrls.size }
    val coroutineScope = rememberCoroutineScope()
    val position = PhotoViewerPosition(index = pagerState.currentPage, total = imageUrls.size)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
            .systemBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, end = 4.dp)
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = onDismiss
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_photo_viewer_close),
                    contentDescription = stringResource(id = R.string.photo_viewer_close),
                    tint = White,
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) { page ->
            ZoomablePhoto(isSettled = pagerState.settledPage == page) {
                imageContent(imageUrls[page], Modifier.fillMaxSize())
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 16.dp)
                .height(44.dp)
        ) {
            if (position.isPreviousVisible) {
                PhotoViewerArrowButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    iconRes = R.drawable.ic_photo_viewer_arrow_left,
                    contentDescription = stringResource(id = R.string.photo_viewer_previous),
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(position.index - 1) }
                    }
                )
            }
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.photo_viewer_counter, position.displayIndex, position.total),
                fontSize = 14.sp,
                color = Gray10,
            )
            if (position.isNextVisible) {
                PhotoViewerArrowButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    iconRes = R.drawable.ic_photo_viewer_arrow_right,
                    contentDescription = stringResource(id = R.string.photo_viewer_next),
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(position.index + 1) }
                    }
                )
            }
        }
    }
}

@Composable
private fun PhotoViewerArrowButton(
    modifier: Modifier,
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp)),
        colors = IconButtonDefaults.iconButtonColors(containerColor = Gray95),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = Green,
        )
    }
}

@Composable
private fun ZoomablePhoto(
    isSettled: Boolean,
    content: @Composable () -> Unit,
) {
    var scale by remember { mutableFloatStateOf(PHOTO_VIEWER_MIN_SCALE) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(isSettled) {
        if (!isSettled) {
            scale = PHOTO_VIEWER_MIN_SCALE
            offsetX = 0f
            offsetY = 0f
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    do {
                        val event = awaitPointerEvent()
                        val pressedCount = event.changes.count { it.pressed }
                        if (pressedCount >= 2 || scale > PHOTO_VIEWER_MIN_SCALE) {
                            val newScale = clampPhotoScale(scale * event.calculateZoom())
                            val pan = event.calculatePan()
                            scale = newScale
                            offsetX = clampPhotoOffset(offsetX + pan.x, newScale, size.width.toFloat())
                            offsetY = clampPhotoOffset(offsetY + pan.y, newScale, size.height.toFloat())
                            event.changes.forEach { change ->
                                if (change.positionChanged()) change.consume()
                            }
                        }
                    } while (event.changes.any { it.pressed })
                    if (scale <= PHOTO_VIEWER_MIN_SCALE) {
                        offsetX = 0f
                        offsetY = 0f
                    }
                }
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offsetX
                translationY = offsetY
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
