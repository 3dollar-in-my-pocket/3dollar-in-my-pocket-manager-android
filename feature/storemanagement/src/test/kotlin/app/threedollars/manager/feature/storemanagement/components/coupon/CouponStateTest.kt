package app.threedollars.manager.feature.storemanagement.components.coupon

import app.threedollars.domain.dto.CouponDto
import app.threedollars.domain.dto.CouponStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CouponStateTest {

    private fun coupon(id: String, status: CouponStatus) = CouponDto(couponId = id, name = "쿠폰$id", status = status)

    // TH-718 TC4
    @Test
    fun `TH718_TC4_쿠폰이_하나도_없으면_빈_상태이고_쿠폰_만들기가_활성이다`() {
        // Given
        val state = CouponState()
            .withActiveCoupon(null)
            .withPage(CouponSegment.IN_USE, CouponPage().withFirstPage(emptyList(), null, false))
        // When
        val isEmpty = state.isInUseEmpty
        // Then
        assertTrue(isEmpty)
        assertTrue(state.canCreateCoupon)
    }

    // TH-718 TC5
    @Test
    fun `TH718_TC5_ACTIVE_쿠폰이_맨_위에_고정되고_아래에_STOPPED_목록이_이어진다`() {
        // Given
        val state = CouponState()
            .withActiveCoupon(coupon("a", CouponStatus.ACTIVE))
            .withPage(CouponSegment.IN_USE, CouponPage().withFirstPage(listOf(coupon("s1", CouponStatus.STOPPED), coupon("s2", CouponStatus.STOPPED)), null, false))
        // When
        val coupons = state.inUseCoupons
        // Then
        assertEquals(listOf("a", "s1", "s2"), coupons.map { it.couponId })
        assertEquals(CouponStatus.ACTIVE, coupons.first().status)
    }

    // TH-718 TC6
    @Test
    fun `TH718_TC6_ACTIVE_쿠폰이_있으면_쿠폰_만들기가_비활성이다`() {
        // Given
        val state = CouponState().withActiveCoupon(coupon("a", CouponStatus.ACTIVE))
        // When
        val canCreate = state.canCreateCoupon
        // Then
        assertFalse(canCreate)
    }

    // TH-718 TC7
    @Test
    fun `TH718_TC7_종료된_쿠폰_세그먼트는_ENDED_상태만_조회한다`() {
        // Given
        val segment = CouponSegment.ENDED
        // When
        val statuses = segment.statuses
        // Then
        assertEquals(listOf(CouponStatus.ENDED), statuses)
        assertEquals(listOf(CouponStatus.STOPPED), CouponSegment.IN_USE.statuses)
    }

    // TH-718 TC8
    @Test
    fun `TH718_TC8_hasMore가_true면_다음_페이지를_이어_붙이고_false면_더_요청하지_않는다`() {
        // Given
        val page = CouponPage().withFirstPage(listOf(coupon("1", CouponStatus.ENDED)), nextCursor = "c1", hasMore = true)
        assertTrue(page.canLoadMore)
        // When
        val next = page.withNextPage(listOf(coupon("2", CouponStatus.ENDED)), nextCursor = null, hasMore = false)
        // Then
        assertEquals(listOf("1", "2"), next.coupons.map { it.couponId })
        assertFalse(next.canLoadMore)
    }

    // TH-718 TC11
    @Test
    fun `TH718_TC11_발급_중지_성공_시_해당_쿠폰이_STOPPED로_목록에_남고_쿠폰_만들기가_활성화된다`() {
        // Given
        val active = coupon("a", CouponStatus.ACTIVE)
        val state = CouponState()
            .withActiveCoupon(active)
            .withPage(CouponSegment.IN_USE, CouponPage().withFirstPage(listOf(coupon("s1", CouponStatus.STOPPED)), null, false))
            .copy(closeTargetCouponId = "a", isCloseConfirmChecked = true)
        // When
        val closed = state.withCouponClosed(active)
        // Then
        assertEquals(null, closed.activeCoupon)
        assertEquals(listOf("a", "s1"), closed.inUseCoupons.map { it.couponId })
        assertEquals(CouponStatus.STOPPED, closed.inUseCoupons.first().status)
        assertTrue(closed.canCreateCoupon)
        assertEquals(null, closed.closeTargetCouponId)
    }

    // TH-718 TC12
    @Test
    fun `TH718_TC12_중지_모달을_취소하면_대상만_해제되고_쿠폰_상태는_그대로다`() {
        // Given
        val active = coupon("a", CouponStatus.ACTIVE)
        val state = CouponState().withActiveCoupon(active).copy(closeTargetCouponId = "a", isCloseConfirmChecked = true)
        // When
        val cancelled = state.copy(closeTargetCouponId = null, isCloseConfirmChecked = false)
        // Then
        assertEquals(active, cancelled.activeCoupon)
        assertEquals(null, cancelled.closeTargetCouponId)
    }
}
