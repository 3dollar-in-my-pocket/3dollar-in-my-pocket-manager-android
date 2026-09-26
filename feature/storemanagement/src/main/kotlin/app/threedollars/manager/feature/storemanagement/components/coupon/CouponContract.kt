package app.threedollars.manager.feature.storemanagement.components.coupon

import app.threedollars.domain.dto.CouponDto
import app.threedollars.domain.dto.CouponStatus
import java.time.LocalDate

internal const val COUPON_PAGE_SIZE = 20
internal const val COUPON_NAME_MAX_LENGTH = 40
internal const val COUPON_UNLIMITED_COUNT = 999
internal const val COUPON_DEFAULT_PERIOD_DAYS = 30L

internal enum class CouponSegment(val title: String, val statuses: List<CouponStatus>) {
    IN_USE(title = "발급 / 사용 중 쿠폰", statuses = listOf(CouponStatus.STOPPED)),
    ENDED(title = "종료된 쿠폰", statuses = listOf(CouponStatus.ENDED)),
}

internal data class CouponPage(
    val coupons: List<CouponDto> = listOf(),
    val isLoaded: Boolean = false,
    val isLoadingMore: Boolean = false,
    val nextCursor: String? = null,
    val hasMore: Boolean = false,
) {
    val canLoadMore: Boolean get() = isLoaded && hasMore && !nextCursor.isNullOrEmpty() && !isLoadingMore

    fun withFirstPage(page: List<CouponDto>, nextCursor: String?, hasMore: Boolean) = copy(
        coupons = page,
        isLoaded = true,
        isLoadingMore = false,
        nextCursor = nextCursor,
        hasMore = hasMore,
    )

    fun withNextPage(page: List<CouponDto>, nextCursor: String?, hasMore: Boolean) = copy(
        coupons = coupons + page,
        isLoadingMore = false,
        nextCursor = nextCursor,
        hasMore = hasMore,
    )
}

internal data class CouponState(
    val storeId: String = "",
    val segment: CouponSegment = CouponSegment.IN_USE,
    val activeCoupon: CouponDto? = null,
    val isActiveLoaded: Boolean = false,
    val inUsePage: CouponPage = CouponPage(),
    val endedPage: CouponPage = CouponPage(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val closeTargetCouponId: String? = null,
    val isCloseConfirmChecked: Boolean = false,
    val errorMessage: String? = null,
) {
    val currentPage: CouponPage get() = if (segment == CouponSegment.IN_USE) inUsePage else endedPage

    val inUseCoupons: List<CouponDto> get() = listOfNotNull(activeCoupon) + inUsePage.coupons

    val isInUseEmpty: Boolean get() = isActiveLoaded && inUsePage.isLoaded && inUseCoupons.isEmpty()

    val isEndedEmpty: Boolean get() = endedPage.isLoaded && endedPage.coupons.isEmpty()

    val canCreateCoupon: Boolean get() = isActiveLoaded && activeCoupon == null

    fun pageFor(segment: CouponSegment) = if (segment == CouponSegment.IN_USE) inUsePage else endedPage

    fun withPage(segment: CouponSegment, page: CouponPage) =
        if (segment == CouponSegment.IN_USE) copy(inUsePage = page) else copy(endedPage = page)

    fun withActiveCoupon(coupon: CouponDto?) = copy(activeCoupon = coupon, isActiveLoaded = true)

    fun withCouponClosed(closed: CouponDto) = copy(
        activeCoupon = if (activeCoupon?.couponId == closed.couponId) null else activeCoupon,
        inUsePage = inUsePage.copy(
            coupons = listOf(closed.copy(status = CouponStatus.STOPPED)) + inUsePage.coupons.filterNot { it.couponId == closed.couponId },
        ),
        closeTargetCouponId = null,
        isCloseConfirmChecked = false,
        isLoading = false,
    )
}

internal enum class CouponCountOption(val title: String, val fixedCount: Int?) {
    UNLIMITED(title = "무제한", fixedCount = COUPON_UNLIMITED_COUNT),
    TEN(title = "10개", fixedCount = 10),
    THIRTY(title = "30개", fixedCount = 30),
    CUSTOM(title = "수량 입력", fixedCount = null),
}

internal data class RegisterCouponState(
    val name: String = "",
    val startDate: LocalDate,
    val endDate: LocalDate,
    val countOption: CouponCountOption = CouponCountOption.UNLIMITED,
    val customCountInput: String = "",
    val nonce: String? = null,
    val isSaving: Boolean = false,
    val showConfirmDialog: Boolean = false,
    val errorMessage: String? = null,
) {
    val maxIssuableCount: Int?
        get() = countOption.fixedCount ?: customCountInput.toIntOrNull()?.takeIf { it > 0 }

    val isSubmitEnabled: Boolean
        get() = name.isNotBlank() && maxIssuableCount != null && !isSaving

    fun withName(input: String) = copy(name = input.take(COUPON_NAME_MAX_LENGTH))

    fun withCustomCount(input: String) = copy(customCountInput = input.filter { it.isDigit() })

    fun withCountOption(option: CouponCountOption) = copy(
        countOption = option,
        customCountInput = if (option == CouponCountOption.CUSTOM) customCountInput else "",
    )

    companion object {
        fun initial(today: LocalDate = LocalDate.now()) = RegisterCouponState(
            startDate = today,
            endDate = today.plusDays(COUPON_DEFAULT_PERIOD_DAYS),
        )
    }
}
