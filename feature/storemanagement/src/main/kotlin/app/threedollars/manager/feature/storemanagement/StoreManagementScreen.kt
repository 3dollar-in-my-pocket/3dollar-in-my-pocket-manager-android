package app.threedollars.manager.feature.storemanagement

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import app.threedollars.common.analytics.AnalyticsLogger
import app.threedollars.common.toDisplayErrorMessage
import app.threedollars.common.ui.Gray0
import app.threedollars.common.ui.Gray30
import app.threedollars.common.ui.Gray95
import app.threedollars.common.ui.Red
import app.threedollars.common.ui.White
import app.threedollars.domain.dto.ContentsDto
import app.threedollars.manager.feature.storemanagement.components.BossCommentScreen
import app.threedollars.manager.feature.storemanagement.components.BusinessScheduleEditScreen
import app.threedollars.manager.feature.storemanagement.components.MyScreen
import app.threedollars.manager.feature.storemanagement.components.ScheduleDay
import app.threedollars.manager.feature.storemanagement.components.SystemAlertDialog
import app.threedollars.manager.feature.storemanagement.components.account.AccountScreen
import app.threedollars.manager.feature.storemanagement.components.coupon.CouponTab
import app.threedollars.manager.feature.storemanagement.components.menumanagement.MenuManagementScreen
import app.threedollars.manager.feature.storemanagement.components.profile.ProfileEditScreen
import app.threedollars.manager.feature.storemanagement.components.review.FeedbackScreen
import app.threedollars.manager.feature.storemanagement.components.review.ReviewContent
import app.threedollars.manager.feature.storemanagement.components.storepost.StorePostTab
import app.threedollars.manager.feature.storemanagement.model.AppearanceDaysVo
import app.threedollars.manager.feature.storemanagement.model.BankTypeVo
import app.threedollars.manager.feature.storemanagement.model.BossStorePatchModel
import app.threedollars.manager.feature.storemanagement.model.BossStoreRetrieveVo
import app.threedollars.manager.feature.storemanagement.model.FeedbackFullVo
import app.threedollars.manager.feature.storemanagement.model.FeedbackTypesVo
import app.threedollars.manager.feature.storemanagement.model.ReviewVo
import app.threedollars.manager.feature.storemanagement.model.StoreCategoriesVo

@Composable
internal fun StoreManagementScreen(
    screenType: ScreenType,
    dialogType: DialogType,
    bossStoreRetrieve: BossStoreRetrieveVo,
    storeCategories: List<StoreCategoriesVo>,
    selectedStoreCategories: List<String>,
    bankTypes: List<BankTypeVo>,
    errorMessage: String?,
    scheduleDays: List<ScheduleDay>,
    appearanceDays: HashMap<String, AppearanceDaysVo>,
    reviews: List<ReviewVo>,
    feedbackFulls: List<FeedbackFullVo>,
    feedbackTypes: List<FeedbackTypesVo>,
    feedbackSpecific: LazyPagingItems<ContentsDto>,
    showCouponNewBadge: Boolean,
    showCouponTooltip: Boolean,
    onScreenTypeUpdate: (ScreenType) -> Unit,
    onCouponTabClick: () -> Unit,
    onDialogTypeUpdate: (DialogType) -> Unit,
    onBossStorePatch: (BossStorePatchModel) -> Unit,
    onAccountNumbersDelete: () -> Unit,
    onMenuPatch: (BossStorePatchModel) -> Unit,
    onStoreCategorySelected: (Int) -> Unit,
    onStartTimeUpdate: (String, String) -> Unit,
    onEndTimeUpdate: (String, String) -> Unit,
    onLocationDescriptionUpdate: (String, String) -> Unit,
    onScheduleDayUpdate: (ScheduleDay) -> Unit,
    onAllReviewNavigate: (String?) -> Unit,
    onUploadPostNavigate: () -> Unit,
    onRegisterCouponNavigate: () -> Unit,
    onStickerClick: (String, String) -> Unit,
) {
    if (dialogType == DialogType.ERROR_DIALOG) {
        SystemAlertDialog(
            message = errorMessage.toDisplayErrorMessage() ?: "요청에 실패했습니다. 잠시 후 다시 시도해주세요.",
            onConfirm = { onDialogTypeUpdate(DialogType.NONE) }
        )
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Gray0,
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            if (screenType == ScreenType.STORE_INFO || screenType == ScreenType.REVIEW_INFO || screenType == ScreenType.STORE_POST || screenType == ScreenType.COUPON) {
                TopBar(
                    screenType = screenType,
                    showCouponNewBadge = showCouponNewBadge,
                    showCouponTooltip = showCouponTooltip,
                    onScreenTypeUpdate = onScreenTypeUpdate,
                    onCouponTabClick = onCouponTabClick,
                )
            }
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(innerPadding)
        ) {
            when (screenType) {
                ScreenType.STORE_INFO -> {
                    Spacer(modifier = Modifier.padding(top = 12.dp))
                    MyScreen(
                        bossStoreRetrieve = bossStoreRetrieve,
                        onScreenTypeUpdate = onScreenTypeUpdate
                    )
                }

                ScreenType.REVIEW_INFO -> {
                    Spacer(modifier = Modifier.padding(top = 12.dp))
                    ReviewContent(
                        subscriberCount = bossStoreRetrieve.subscriberCount,
                        rating = bossStoreRetrieve.rating,
                        reviewTotalCount = bossStoreRetrieve.reviewTotalCount,
                        storeName = bossStoreRetrieve.name,
                        reviews = reviews,
                        feedbackFulls = feedbackFulls,
                        feedbackTypes = feedbackTypes,
                        onScreenTypeUpdate = onScreenTypeUpdate,
                        onAllReviewNavigate = onAllReviewNavigate,
                        onStickerClick = onStickerClick
                    )
                }

                ScreenType.STORE_POST -> {
                    StorePostTab(
                        storeId = bossStoreRetrieve.bossStoreId,
                        onUploadNavigate = onUploadPostNavigate
                    )
                }

                ScreenType.COUPON -> {
                    CouponTab(
                        storeId = bossStoreRetrieve.bossStoreId,
                        onRegisterNavigate = onRegisterCouponNavigate
                    )
                }

                ScreenType.FEEDBACK -> {
                    FeedbackScreen(
                        feedbackFulls = feedbackFulls,
                        feedbackTypes = feedbackTypes,
                        feedbackSpecific = feedbackSpecific,
                        onScreenTypeUpdate = onScreenTypeUpdate
                    )
                }


                ScreenType.PROFILE_EDIT -> {
                    ProfileEditScreen(
                        bossStoreRetrieve = bossStoreRetrieve,
                        storeCategories = storeCategories,
                        selectedStoreCategories = selectedStoreCategories,
                        onScreenTypeUpdate = onScreenTypeUpdate,
                        onBossStorePatch = onBossStorePatch,
                        onStoreCategorySelected = onStoreCategorySelected
                    )
                }

                ScreenType.BOSS_COMMENT -> {
                    BossCommentScreen(
                        bossStoreRetrieve = bossStoreRetrieve,
                        onScreenTypeUpdate = onScreenTypeUpdate,
                        onBossStorePatch = onBossStorePatch
                    )
                }

                ScreenType.MENU_MANAGEMENT -> {
                    MenuManagementScreen(
                        bossStoreRetrieve = bossStoreRetrieve,
                        dialogType = dialogType,
                        errorMessage = errorMessage,
                        onMenuPatch = onMenuPatch,
                        onScreenTypeUpdate = onScreenTypeUpdate
                    )
                }

                ScreenType.ACCOUNT -> {
                    AccountScreen(
                        bossStoreRetrieve = bossStoreRetrieve,
                        bankTypes = bankTypes,
                        onScreenTypeUpdate = onScreenTypeUpdate,
                        onBossStorePatch = onBossStorePatch,
                        onAccountNumbersDelete = onAccountNumbersDelete
                    )
                }

                ScreenType.BUSINESS_SCHEDULE_EDIT -> {
                    BusinessScheduleEditScreen(
                        bossStoreRetrieve = bossStoreRetrieve,
                        scheduleDays = scheduleDays,
                        appearanceDays = appearanceDays,
                        onScreenTypeUpdate = onScreenTypeUpdate,
                        onBossStorePatch = onBossStorePatch,
                        onStartTimeUpdate = onStartTimeUpdate,
                        onEndTimeUpdate = onEndTimeUpdate,
                        onLocationDescriptionUpdate = onLocationDescriptionUpdate,
                        onScheduleDayUpdate = onScheduleDayUpdate,
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    onScreenTypeUpdate: (ScreenType) -> Unit,
    onCouponTabClick: () -> Unit,
    screenType: ScreenType,
    showCouponNewBadge: Boolean,
    showCouponTooltip: Boolean,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray0)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .horizontalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            SubTab(title = "가게정보", selected = screenType == ScreenType.STORE_INFO) {
                logTapMyTopTab(context, StoreManagementLog.TAB_STORE_INFO)
                onScreenTypeUpdate(ScreenType.STORE_INFO)
            }
            SubTab(title = "리뷰통계", selected = screenType == ScreenType.REVIEW_INFO) {
                logTapMyTopTab(context, StoreManagementLog.TAB_STATISTICS)
                onScreenTypeUpdate(ScreenType.REVIEW_INFO)
            }
            SubTab(title = "가게소식", selected = screenType == ScreenType.STORE_POST) {
                logTapMyTopTab(context, StoreManagementLog.TAB_STORE_POST)
                onScreenTypeUpdate(ScreenType.STORE_POST)
            }
            Row(verticalAlignment = Alignment.Top) {
                SubTab(title = "쿠폰 관리", selected = screenType == ScreenType.COUPON) {
                    logTapMyTopTab(context, StoreManagementLog.TAB_COUPON)
                    onCouponTabClick()
                }
                if (showCouponNewBadge) {
                    Text(
                        text = "N",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = White,
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .background(Red, CircleShape)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
        }
        if (showCouponTooltip) {
            CouponTooltip(
                modifier = Modifier.padding(start = 24.dp, bottom = 8.dp),
                onClick = onCouponTabClick,
            )
        }
    }
}

@Composable
private fun SubTab(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = if (selected) FontWeight.Bold else null,
        color = if (selected) Gray95 else Gray30,
        maxLines = 1,
        softWrap = false,
        modifier = Modifier.noRippleClickable(onClick),
    )
}

private fun logTapMyTopTab(context: Context, tab: String) {
    AnalyticsLogger.logEvent(
        context,
        StoreManagementLog.EVENT_TAP_MY_TOP_TAB,
        mapOf(
            StoreManagementLog.PARAM_SCREEN to StoreManagementLog.SCREEN_MY_STORE_INFO,
            StoreManagementLog.PARAM_TAB to tab,
        ),
    )
}

@Composable
private fun CouponTooltip(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "\uD83C\uDF9F\uFE0F 쿠폰으로 단골을 만들어보세요!",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = White,
            modifier = Modifier
                .padding(end = 16.dp)
                .background(Gray95, RoundedCornerShape(8.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}
