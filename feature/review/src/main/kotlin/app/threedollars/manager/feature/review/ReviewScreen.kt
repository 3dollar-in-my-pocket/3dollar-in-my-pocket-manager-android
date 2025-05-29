package app.threedollars.manager.feature.review

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import app.threedollars.common.ui.CircleProgressBar
import app.threedollars.manager.feature.review.ScreenType.ALL_REVIEW
import app.threedollars.manager.feature.review.ScreenType.LOADING
import app.threedollars.manager.feature.review.ScreenType.REVIEW_DETAIL
import app.threedollars.manager.feature.review.components.AllReviewContent
import app.threedollars.manager.feature.review.components.ReviewDetailScreen
import app.threedollars.manager.feature.review.model.CommentPresetVo
import app.threedollars.manager.feature.review.model.ReviewVo

@Composable
internal fun ReviewScreen(
    screenType: ScreenType,
    dialogType: DialogType,
    reviewTotalCount: Int,
    rating: Double,
    reviewFilterType: ReviewFilterType,
    storeReviewPaging: LazyPagingItems<ReviewVo>,
    selectedReview: ReviewVo,
    commentPresets: List<CommentPresetVo>,
    selectEditPresetText: String,
    selectEditPresetId: String,
    onScreenTypeUpdate: (ScreenType) -> Unit,
    onDialogTypeUpdate: (DialogType) -> Unit,
    onReviewFilterTypeUpdate: (ReviewFilterType) -> Unit,
    onReviewDetailClick: (String) -> Unit,
    onStoreManagementNavigate: () -> Unit,
    onReportClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onCommentDeleteClick: () -> Unit,
    onPresetClick: () -> Unit,
    onPresetWriteClick: (String) -> Unit,
    onPresetEditClick: (String, String) -> Unit,
    onPresetDeleteClick: (String) -> Unit,
    onPresetEditMenuClick: (String, String) -> Unit,
    onStickerClick: (String, String, Boolean) -> Unit,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    when (screenType) {
        ALL_REVIEW -> {
            AllReviewContent(
                reviewTotalCount = reviewTotalCount,
                rating = rating,
                reviewFilterType = reviewFilterType,
                coroutineScope = coroutineScope,
                listState = listState,
                onReviewFilterTypeUpdate = onReviewFilterTypeUpdate,
                storeReviewPaging = storeReviewPaging,
                onReviewDetailClick = onReviewDetailClick,
                onStoreManagementNavigate = onStoreManagementNavigate,
                onStickerClick = onStickerClick
            )
        }

        REVIEW_DETAIL -> {
            ReviewDetailScreen(
                reviewVo = selectedReview,
                commentPresets = commentPresets,
                dialogType = dialogType,
                selectEditPresetText = selectEditPresetText,
                selectEditPresetId = selectEditPresetId,
                onScreenTypeUpdate = onScreenTypeUpdate,
                onDialogTypeUpdate = onDialogTypeUpdate,
                onReportClick = onReportClick,
                onCommentClick = onCommentClick,
                onCommentDeleteClick = onCommentDeleteClick,
                onPresetClick = onPresetClick,
                onPresetWriteClick = onPresetWriteClick,
                onPresetEditClick = onPresetEditClick,
                onPresetDeleteClick = onPresetDeleteClick,
                onPresetEditMenuClick = onPresetEditMenuClick,
                onStickerClick = onStickerClick
            )
        }

        LOADING -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircleProgressBar(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}