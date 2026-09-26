package app.threedollars.manager.feature.storemanagement.components.coupon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.threedollars.common.Resource
import app.threedollars.domain.dto.CouponStatus
import app.threedollars.domain.usecase.CloseCouponUseCase
import app.threedollars.domain.usecase.CreateNonceUseCase
import app.threedollars.domain.usecase.GetCouponListUseCase
import app.threedollars.domain.usecase.RegisterCouponUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class CouponViewModel @Inject constructor(
    private val getCouponListUseCase: GetCouponListUseCase,
    private val createNonceUseCase: CreateNonceUseCase,
    private val registerCouponUseCase: RegisterCouponUseCase,
    private val closeCouponUseCase: CloseCouponUseCase,
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(CouponState())
    val stateFlow: StateFlow<CouponState> = _stateFlow.asStateFlow()

    private val _registerStateFlow = MutableStateFlow(RegisterCouponState.initial())
    val registerStateFlow: StateFlow<RegisterCouponState> = _registerStateFlow.asStateFlow()

    private val _toastFlow = MutableSharedFlow<String>()
    val toastFlow = _toastFlow.asSharedFlow()

    private val _registerFinishedFlow = MutableSharedFlow<Unit>()
    val registerFinishedFlow = _registerFinishedFlow.asSharedFlow()

    fun onTabEntered(storeId: String) {
        if (storeId.isEmpty()) return
        val state = _stateFlow.value
        if (state.storeId == storeId && state.isActiveLoaded) return
        _stateFlow.update { it.copy(storeId = storeId) }
        refresh()
    }

    fun selectSegment(segment: CouponSegment) {
        _stateFlow.update { it.copy(segment = segment) }
        if (!_stateFlow.value.pageFor(segment).isLoaded) {
            loadFirstPage(segment)
        }
    }

    fun refresh() {
        val storeId = _stateFlow.value.storeId
        if (storeId.isEmpty()) return
        _stateFlow.update { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            val active = async { getCouponListUseCase(storeId, listOf(CouponStatus.ACTIVE), size = 1) }
            val inUse = async { getCouponListUseCase(storeId, CouponSegment.IN_USE.statuses, size = COUPON_PAGE_SIZE) }
            val ended = async { getCouponListUseCase(storeId, CouponSegment.ENDED.statuses, size = COUPON_PAGE_SIZE) }
            val activeResult = active.await()
            val inUseResult = inUse.await()
            val endedResult = ended.await()
            val failure = listOf(activeResult, inUseResult, endedResult).firstOrNull { it !is Resource.Success || it.data == null }
            if (failure != null) {
                _stateFlow.update { it.copy(isRefreshing = false, errorMessage = failure.errorMessage.orDefault()) }
                return@launch
            }
            _stateFlow.update { state ->
                state
                    .withActiveCoupon(activeResult.data!!.contents.firstOrNull())
                    .withPage(CouponSegment.IN_USE, state.inUsePage.withFirstPage(inUseResult.data!!.contents, inUseResult.data!!.cursor?.nextCursor, inUseResult.data!!.cursor?.hasMore ?: false))
                    .withPage(CouponSegment.ENDED, state.endedPage.withFirstPage(endedResult.data!!.contents, endedResult.data!!.cursor?.nextCursor, endedResult.data!!.cursor?.hasMore ?: false))
                    .copy(isRefreshing = false)
            }
        }
    }

    private fun loadFirstPage(segment: CouponSegment) {
        val storeId = _stateFlow.value.storeId
        viewModelScope.launch {
            val result = getCouponListUseCase(storeId, segment.statuses, size = COUPON_PAGE_SIZE)
            if (result is Resource.Success && result.data != null) {
                val data = result.data!!
                _stateFlow.update { it.withPage(segment, it.pageFor(segment).withFirstPage(data.contents, data.cursor?.nextCursor, data.cursor?.hasMore ?: false)) }
            } else {
                _stateFlow.update { it.copy(errorMessage = result.errorMessage.orDefault()) }
            }
        }
    }

    fun loadNextPage() {
        val state = _stateFlow.value
        val segment = state.segment
        val page = state.pageFor(segment)
        if (!page.canLoadMore) return
        _stateFlow.update { it.withPage(segment, page.copy(isLoadingMore = true)) }
        viewModelScope.launch {
            val result = getCouponListUseCase(state.storeId, segment.statuses, size = COUPON_PAGE_SIZE, cursor = page.nextCursor)
            if (result is Resource.Success && result.data != null) {
                val data = result.data!!
                _stateFlow.update { it.withPage(segment, it.pageFor(segment).withNextPage(data.contents, data.cursor?.nextCursor, data.cursor?.hasMore ?: false)) }
            } else {
                _stateFlow.update { it.withPage(segment, it.pageFor(segment).copy(isLoadingMore = false)).copy(errorMessage = result.errorMessage.orDefault()) }
            }
        }
    }

    fun requestClose(couponId: String) {
        _stateFlow.update { it.copy(closeTargetCouponId = couponId, isCloseConfirmChecked = false) }
    }

    fun toggleCloseConfirmChecked() {
        _stateFlow.update { it.copy(isCloseConfirmChecked = !it.isCloseConfirmChecked) }
    }

    fun cancelClose() {
        _stateFlow.update { it.copy(closeTargetCouponId = null, isCloseConfirmChecked = false) }
    }

    fun confirmClose() {
        val state = _stateFlow.value
        val couponId = state.closeTargetCouponId ?: return
        if (!state.isCloseConfirmChecked) return
        val target = state.inUseCoupons.firstOrNull { it.couponId == couponId } ?: return
        _stateFlow.update { it.copy(isLoading = true, closeTargetCouponId = null) }
        viewModelScope.launch {
            val result = closeCouponUseCase(storeId = state.storeId, couponId = couponId)
            if (result is Resource.Success) {
                _stateFlow.update { it.withCouponClosed(target) }
                _toastFlow.emit("쿠폰 발급을 중지했어요.")
                refresh()
            } else {
                _stateFlow.update { it.copy(isLoading = false, errorMessage = result.errorMessage.orDefault()) }
            }
        }
    }

    fun clearError() {
        _stateFlow.update { it.copy(errorMessage = null) }
    }

    fun startRegister(today: LocalDate = LocalDate.now()) {
        _registerStateFlow.value = RegisterCouponState.initial(today)
        viewModelScope.launch {
            val result = createNonceUseCase()
            if (result is Resource.Success && result.data != null) {
                _registerStateFlow.update { it.copy(nonce = result.data) }
            } else {
                _registerStateFlow.update { it.copy(errorMessage = result.errorMessage.orDefault()) }
            }
        }
    }

    fun updateName(name: String) = _registerStateFlow.update { it.withName(name) }

    fun updateStartDate(date: LocalDate) = _registerStateFlow.update { it.copy(startDate = date) }

    fun updateEndDate(date: LocalDate) = _registerStateFlow.update { it.copy(endDate = date) }

    fun selectCountOption(option: CouponCountOption) = _registerStateFlow.update { it.withCountOption(option) }

    fun updateCustomCount(input: String) = _registerStateFlow.update { it.withCustomCount(input) }

    fun showRegisterConfirm(show: Boolean) = _registerStateFlow.update { it.copy(showConfirmDialog = show) }

    fun clearRegisterError() = _registerStateFlow.update { it.copy(errorMessage = null) }

    fun register() {
        val registerState = _registerStateFlow.value
        if (!registerState.isSubmitEnabled) return
        val request = CouponFormatter.toRegisterDto(registerState) ?: return
        val storeId = _stateFlow.value.storeId
        _registerStateFlow.update { it.copy(isSaving = true, showConfirmDialog = false) }
        viewModelScope.launch {
            val nonce = registerState.nonce ?: createNonceUseCase().let { nonceResult ->
                nonceResult.data ?: run {
                    _registerStateFlow.update { it.copy(isSaving = false, errorMessage = nonceResult.errorMessage.orDefault()) }
                    return@launch
                }
            }
            val result = registerCouponUseCase(storeId = storeId, nonce = nonce, request = request)
            if (result is Resource.Success) {
                _registerStateFlow.update { it.copy(isSaving = false) }
                _toastFlow.emit("쿠폰이 발급되었어요.")
                refresh()
                _registerFinishedFlow.emit(Unit)
            } else {
                _registerStateFlow.update { it.copy(isSaving = false, errorMessage = result.errorMessage.orDefault()) }
            }
        }
    }

    private fun String?.orDefault() = this?.takeIf { it.isNotBlank() } ?: "요청에 실패했습니다. 잠시 후 다시 시도해주세요."
}
