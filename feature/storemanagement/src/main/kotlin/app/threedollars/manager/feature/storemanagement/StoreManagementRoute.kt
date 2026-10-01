package app.threedollars.manager.feature.storemanagement

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import app.threedollars.common.REVIEW_LIST
import app.threedollars.common.ext.toast
import app.threedollars.common.ui.DoubleBackExitHandler


@Composable
fun StoreManagementRoute(
    onAllReviewNavigate: (String?) -> Unit,
    onUploadPostNavigate: () -> Unit,
    onRegisterCouponNavigate: () -> Unit,
    screenType: String?
) {
    val viewModel: StoreManagementViewModel = hiltViewModel()

    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    val feedbackSpecific = viewModel.feedbackSpecific.collectAsLazyPagingItems()

    val currentScreenType = uiState.screenType

    LaunchedEffect(Unit) {
        viewModel.loadCouponGuide()
        viewModel.loadMessageGuide()
        screenType?.let {
            val defaultScreenType = when(screenType){
                REVIEW_LIST -> ScreenType.REVIEW_INFO
                else -> ScreenType.STORE_INFO
            }
            viewModel.updateScreenType(defaultScreenType)
        }
    }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.toastFlow.collect { context.toast(it) }
    }

    LaunchedEffect(currentScreenType) {
        viewModel.getBossStoreRetrieveMe()

        when (currentScreenType) {
            ScreenType.REVIEW_INFO -> {
                viewModel.getFeedbackType()
                viewModel.getFeedbackFull()
                viewModel.getStoreReviews()
            }

            ScreenType.PROFILE_EDIT -> {
                viewModel.getCategory()
            }

            ScreenType.ACCOUNT -> {
                viewModel.getBankEnum()
            }

            ScreenType.FEEDBACK -> {
                viewModel.getFeedbackSpecific()
            }

            else -> {}
        }
    }
    if (currentScreenType == ScreenType.STORE_INFO) {
        DoubleBackExitHandler()
    } else {
        BackHandler {
            when (currentScreenType) {
                ScreenType.REVIEW_INFO,
                ScreenType.STORE_POST,
                ScreenType.MESSAGE,
                ScreenType.COUPON,
                ScreenType.PROFILE_EDIT,
                ScreenType.BUSINESS_SCHEDULE_EDIT,
                ScreenType.MENU_MANAGEMENT,
                ScreenType.BOSS_COMMENT,
                ScreenType.ACCOUNT,
                    -> viewModel.updateScreenType(ScreenType.STORE_INFO)
                ScreenType.FEEDBACK -> viewModel.updateScreenType(ScreenType.REVIEW_INFO)
                else -> {}
            }
        }
    }
    StoreManagementScreen(
        screenType = currentScreenType,
        dialogType = uiState.dialogType,
        bossStoreRetrieve = uiState.bossStoreRetrieve,
        storeCategories = uiState.storeCategories,
        selectedStoreCategories = uiState.selectedStoreCategories,
        bankTypes = uiState.bankTypes,
        errorMessage = uiState.errorMessage,
        scheduleDays = uiState.scheduleDays,
        appearanceDays = uiState.appearanceDays,
        reviews = uiState.reviews,
        feedbackFulls = uiState.feedbackFulls,
        feedbackTypes = uiState.feedbackTypes,
        feedbackSpecific = feedbackSpecific,
        showCouponNewBadge = uiState.showCouponNewBadge,
        showCouponTooltip = uiState.showCouponTooltip,
        showMessageTooltip = uiState.showMessageTooltip,
        onScreenTypeUpdate = viewModel::updateScreenType,
        onCouponTabClick = viewModel::onCouponTabClicked,
        onMessageTabClick = viewModel::onMessageTabClicked,
        onDialogTypeUpdate = viewModel::updateDialogType,
        onBossStorePatch = viewModel::patchBossStore,
        onAccountNumbersDelete = viewModel::deleteAccountNumbers,
        onMenuPatch = viewModel::patchMenu,
        onStoreCategorySelected = viewModel::categorySelection,
        onStartTimeUpdate = viewModel::updateDaysStartTime,
        onEndTimeUpdate = viewModel::updateDaysEndTime,
        onLocationDescriptionUpdate = viewModel::updateDaysLocationDescription,
        onScheduleDayUpdate = viewModel::updateScheduleDay,
        onAllReviewNavigate = onAllReviewNavigate,
        onUploadPostNavigate = onUploadPostNavigate,
        onRegisterCouponNavigate = onRegisterCouponNavigate,
        onStickerClick = viewModel::putStickersReplace
    )
}
