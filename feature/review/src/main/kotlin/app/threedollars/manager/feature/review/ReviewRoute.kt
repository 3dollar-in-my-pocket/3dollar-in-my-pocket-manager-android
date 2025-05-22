package app.threedollars.manager.feature.review

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import app.threedollars.manager.feature.review.ScreenType.ALL_REVIEW
import app.threedollars.manager.feature.review.ScreenType.REVIEW_DETAIL
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ReviewRoute(
    onStoreManagementNavigate: () -> Unit,
) {
    val viewModel: ReviewViewModel = hiltViewModel()

    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    val storeReviewPaging = viewModel.storeReviewPaging.collectAsLazyPagingItems()

    val screenType = uiState.screenType

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.toastFlow.collectLatest { message ->
            message?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }


    BackHandler {
        when (screenType) {
            ALL_REVIEW -> {
                onStoreManagementNavigate()
            }

            REVIEW_DETAIL -> {
                viewModel.updateScreenType(ALL_REVIEW)
            }

            else -> {}
        }
    }
    LaunchedEffect(screenType) {
        when (screenType) {
            ALL_REVIEW -> {
                viewModel.getBossStoreRetrieveMe()
            }

            else -> {}
        }
    }
    ReviewScreen(
        screenType = uiState.screenType,
        dialogType = uiState.dialogType,
        reviewTotalCount = uiState.bossStoreRetrieve.reviewTotalCount,
        rating = uiState.bossStoreRetrieve.rating,
        reviewFilterType = uiState.reviewFilterType,
        selectedReview = uiState.selectedReview,
        commentPresets = uiState.commentPresets,
        selectEditPresetText = uiState.selectEditPresetText,
        selectEditPresetId = uiState.selectEditPresetId,
        storeReviewPaging = storeReviewPaging,
        onDialogTypeUpdate = viewModel::updateDialogType,
        onScreenTypeUpdate = viewModel::updateScreenType,
        onReviewFilterTypeUpdate = viewModel::updateReviewFilterType,
        onReviewDetailClick = { reviewId ->
            viewModel.getStoreReviewDetail(reviewId = reviewId)
        },
        onStoreManagementNavigate = onStoreManagementNavigate,
        onReportClick = viewModel::reportStoreReview,
        onCommentClick = viewModel::postStoreReviewComment,
        onCommentDeleteClick = viewModel::deleteStoreReviewComment,
        onPresetClick = viewModel::getStoreCommentPresets,
        onPresetWriteClick = { body ->
            viewModel.postStoreCommentPreset(body = body)
        },
        onPresetEditClick = viewModel::patchStoreCommentPreset,
        onPresetDeleteClick = viewModel::deleteStoreCommentPreset,
        onPresetEditMenuClick = viewModel::updateEditPreset,
        onStickerClick = viewModel::putStickersReplace
    )
}
