package app.threedollars.manager.feature.review

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Red
import app.threedollars.manager.feature.review.ScreenType.*
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
    onPresetEditMenuClick: (String, String) -> Unit
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
                onStoreManagementNavigate = onStoreManagementNavigate
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
                onPresetEditMenuClick = onPresetEditMenuClick
            )
        }
    }
}