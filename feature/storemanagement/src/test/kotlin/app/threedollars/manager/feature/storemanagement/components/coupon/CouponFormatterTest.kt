package app.threedollars.manager.feature.storemanagement.components.coupon

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.time.LocalDate

class CouponFormatterTest {

    // TH-718 TC9
    @Test
    fun `TH718_TC9_사용_기간은_yyyy_MM_dd_형식으로_표시된다`() {
        // Given
        val start = "2026-09-27T00:00:00"
        val end = "2026-10-27T23:59:59"
        // When
        val text = CouponFormatter.formatPeriod(start, end)
        // Then
        assertEquals("2026.09.27 ~ 2026.10.27", text)
    }

    // TH-718 TC16
    @Test
    fun `TH718_TC16_날짜_행은_yyyy_M_d_요일_형식으로_표시된다`() {
        // Given
        val date = LocalDate.of(2026, 9, 3)
        // When
        val text = CouponFormatter.formatPickerDate(date)
        // Then
        assertEquals("2026. 9. 3 목요일", text)
    }

    // TH-718 TC21
    @Test
    fun `TH718_TC21_등록_페이로드는_시작일_T00_00_00_종료일_T23_59_59_무제한_999_description_null이다`() {
        // Given
        val state = RegisterCouponState.initial(LocalDate.of(2026, 9, 27)).withName("1000원 할인")
        // When
        val dto = CouponFormatter.toRegisterDto(state)
        // Then
        assertNotNull(dto)
        assertEquals("2026-09-27T00:00:00", dto!!.startDateTime)
        assertEquals("2026-10-27T23:59:59", dto.endDateTime)
        assertEquals(999, dto.maxIssuableCount)
        assertEquals(null, dto.description)
        assertEquals("1000원 할인", dto.name)
    }

    // TH-718 TC9
    @Test
    fun `TH718_TC9_파싱할_수_없는_날짜는_원문을_그대로_표시해_크래시가_없다`() {
        // Given
        val raw = "invalid"
        // When
        val text = CouponFormatter.formatServerDate(raw)
        // Then
        assertEquals("invalid", text)
    }
}
