package app.threedollars.manager.feature.storemanagement.components.coupon

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class RegisterCouponStateTest {

    private val today = LocalDate.of(2026, 9, 27)

    // TH-718 TC13
    @Test
    fun `TH718_TC13_진입_시_시작일은_오늘_종료일은_30일_후_수량은_무제한이고_발급_버튼은_비활성이다`() {
        // Given
        val state = RegisterCouponState.initial(today)
        // When
        val enabled = state.isSubmitEnabled
        // Then
        assertEquals(today, state.startDate)
        assertEquals(today.plusDays(30), state.endDate)
        assertEquals(CouponCountOption.UNLIMITED, state.countOption)
        assertEquals("", state.name)
        assertFalse(enabled)
    }

    // TH-718 TC14
    @Test
    fun `TH718_TC14_쿠폰명은_40자에서_잘린다`() {
        // Given
        val state = RegisterCouponState.initial(today)
        // When
        val typed = state.withName("가".repeat(45))
        // Then
        assertEquals(COUPON_NAME_MAX_LENGTH, typed.name.length)
    }

    // TH-718 TC15
    @Test
    fun `TH718_TC15_쿠폰명만_입력하면_기본값으로_발급_버튼이_활성화된다`() {
        // Given
        val state = RegisterCouponState.initial(today)
        // When
        val typed = state.withName("1000원 할인")
        // Then
        assertTrue(typed.isSubmitEnabled)
        assertEquals(COUPON_UNLIMITED_COUNT, typed.maxIssuableCount)
    }

    // TH-718 TC17
    @Test
    fun `TH718_TC17_수량_입력은_숫자만_받고_비어_있으면_비활성_다른_옵션_선택_시_입력이_비워진다`() {
        // Given
        val state = RegisterCouponState.initial(today).withName("쿠폰").withCountOption(CouponCountOption.CUSTOM)
        assertFalse(state.isSubmitEnabled)
        // When
        val typed = state.withCustomCount("1a2b")
        val switched = typed.withCountOption(CouponCountOption.TEN)
        // Then
        assertEquals("12", typed.customCountInput)
        assertEquals(12, typed.maxIssuableCount)
        assertTrue(typed.isSubmitEnabled)
        assertEquals("", switched.customCountInput)
        assertEquals(10, switched.maxIssuableCount)
    }

    // TH-718 TC19
    @Test
    fun `TH718_TC19_저장_중에는_발급_버튼이_비활성이다`() {
        // Given
        val state = RegisterCouponState.initial(today).withName("쿠폰")
        // When
        val saving = state.copy(isSaving = true)
        // Then
        assertFalse(saving.isSubmitEnabled)
    }
}
