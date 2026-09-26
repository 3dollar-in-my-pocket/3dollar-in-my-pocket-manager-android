package app.threedollars.manager.feature.storemanagement.components.coupon

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.threedollars.common.BaseDialog
import app.threedollars.common.analytics.ScreenViewLogEffect
import app.threedollars.common.ext.toast
import app.threedollars.common.ui.CircleProgressBar
import app.threedollars.common.ui.Gray0
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Gray40
import app.threedollars.common.ui.Gray5
import app.threedollars.common.ui.Gray50
import app.threedollars.common.ui.Gray95
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.Red
import app.threedollars.common.ui.White
import app.threedollars.domain.dto.CouponDto
import app.threedollars.manager.feature.storemanagement.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
internal fun CouponTab(
    storeId: String,
    onRegisterNavigate: () -> Unit,
) {
    val viewModel: CouponViewModel = hiltViewModel()
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ScreenViewLogEffect(screenName = "couponTab")

    LaunchedEffect(storeId) {
        if (storeId.isNotEmpty()) viewModel.onTabEntered(storeId)
    }
    LaunchedEffect(Unit) {
        viewModel.toastFlow.collectLatest { context.toast(it) }
    }

    CouponScreen(
        uiState = uiState,
        onSegmentSelect = viewModel::selectSegment,
        onRefresh = viewModel::refresh,
        onLoadMore = viewModel::loadNextPage,
        onCreateClick = {
            viewModel.startRegister()
            onRegisterNavigate()
        },
        onCloseClick = { viewModel.requestClose(it.couponId) },
        onCloseCheckToggle = viewModel::toggleCloseConfirmChecked,
        onCloseCancel = viewModel::cancelClose,
        onCloseConfirm = viewModel::confirmClose,
        onErrorDismiss = viewModel::clearError,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CouponScreen(
    uiState: CouponState,
    onSegmentSelect: (CouponSegment) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onCreateClick: () -> Unit,
    onCloseClick: (CouponDto) -> Unit,
    onCloseCheckToggle: () -> Unit,
    onCloseCancel: () -> Unit,
    onCloseConfirm: () -> Unit,
    onErrorDismiss: () -> Unit,
) {
    ScreenViewLogEffect(screenName = "coupon")

    if (uiState.closeTargetCouponId != null) {
        CouponCloseDialog(
            isChecked = uiState.isCloseConfirmChecked,
            onCheckToggle = onCloseCheckToggle,
            onCancel = onCloseCancel,
            onConfirm = onCloseConfirm,
        )
    }
    if (uiState.errorMessage != null) {
        BaseDialog(title = "Error", message = uiState.errorMessage, confirmText = "확인", onConfirm = onErrorDismiss)
    }

    Box(modifier = Modifier.fillMaxSize().background(Gray0)) {
        Column(modifier = Modifier.fillMaxSize()) {
            CouponSegmentControl(
                selected = uiState.segment,
                onSelect = onSegmentSelect,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            )
            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = onRefresh,
                modifier = Modifier.fillMaxSize(),
            ) {
                val coupons = if (uiState.segment == CouponSegment.IN_USE) uiState.inUseCoupons else uiState.endedPage.coupons
                val isEmpty = if (uiState.segment == CouponSegment.IN_USE) uiState.isInUseEmpty else uiState.isEndedEmpty
                when {
                    isEmpty -> CouponEmptyView()
                    coupons.isNotEmpty() -> CouponList(
                        coupons = coupons,
                        page = uiState.currentPage,
                        onLoadMore = onLoadMore,
                        onCloseClick = onCloseClick,
                    )
                    else -> Spacer(modifier = Modifier.fillMaxSize())
                }
            }
        }
        if (uiState.isLoading) {
            CircleProgressBar(modifier = Modifier.align(Alignment.Center))
        }
        CreateCouponButton(
            enabled = uiState.canCreateCoupon,
            onClick = onCreateClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 12.dp),
        )
    }
}

@Composable
private fun CouponSegmentControl(
    selected: CouponSegment,
    onSelect: (CouponSegment) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Gray5, RoundedCornerShape(10.dp))
            .padding(4.dp),
    ) {
        CouponSegment.entries.forEach { segment ->
            val isSelected = segment == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .background(if (isSelected) White else Gray5, RoundedCornerShape(8.dp))
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onSelect(segment) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = segment.title,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) Gray100 else Gray40,
                )
            }
        }
    }
}

@Composable
private fun CouponList(
    coupons: List<CouponDto>,
    page: CouponPage,
    onLoadMore: () -> Unit,
    onCloseClick: (CouponDto) -> Unit,
) {
    val listState = rememberLazyListState()
    LaunchedEffect(listState, coupons.size, page.canLoadMore) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .filter { last -> last != null && last >= coupons.lastIndex }
            .collect { if (page.canLoadMore) onLoadMore() }
    }
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(coupons, key = { it.couponId }) { coupon ->
            CouponCard(coupon = coupon, onCloseClick = onCloseClick)
        }
        if (page.isLoadingMore) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                    CircleProgressBar(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
private fun CouponEmptyView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = buildAnnotatedString {
                append("쿠폰으로 ")
                withStyle(SpanStyle(color = Green)) { append("매출도, 단골도") }
                append(" 늘려보세요!")
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Gray95,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(R.drawable.img_coupon_empty),
            contentDescription = null,
            modifier = Modifier.padding(horizontal = 24.dp),
        )
    }
}

@Composable
private fun CreateCouponButton(enabled: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val color = if (enabled) Green else Gray40
    Row(
        modifier = modifier
            .then(if (enabled) Modifier.shadow(6.dp, RoundedCornerShape(22.dp), ambientColor = Green, spotColor = Green) else Modifier)
            .background(color, RoundedCornerShape(22.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .height(44.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_coupon_solid),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
        )
        Text(text = "쿠폰 만들기", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = White)
    }
}

@Composable
private fun CouponCloseDialog(
    isChecked: Boolean,
    onCheckToggle: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    Dialog(onDismissRequest = onCancel) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White, RoundedCornerShape(20.dp))
                .padding(24.dp),
        ) {
            Text(text = "쿠폰 발급을 중지할까요?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Gray100)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "⚠️ 쿠폰 발급이 중지되면, 고객은 더 이상 새로 발급받을 수 없습니다.", fontSize = 13.sp, color = Gray50)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "⚠️ 단, 이미 발급된 쿠폰은 사용 기간까지 정상적으로 사용할 수 있어요.", fontSize = 13.sp, color = Red)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "*정당하게 발급된 쿠폰 사용을 거부하실 경우, 앱 이용에 제한이 있을 수 있으며, 그로 인한 불이익은 사장님께 책임이 있을 수 있습니다.",
                fontSize = 12.sp,
                color = Gray50,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { onCheckToggle() },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onCheckToggle() },
                    colors = CheckboxDefaults.colors(checkedColor = Green),
                )
                Text(text = "위의 내용을 모두 확인하였습니다.", fontSize = 14.sp, color = Gray100)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(if (isChecked) Red else Gray40, RoundedCornerShape(12.dp))
                    .clickable(enabled = isChecked, onClick = onConfirm),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "발급 중지하기", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable(onClick = onCancel),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "취소", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Gray50)
            }
        }
    }
}
