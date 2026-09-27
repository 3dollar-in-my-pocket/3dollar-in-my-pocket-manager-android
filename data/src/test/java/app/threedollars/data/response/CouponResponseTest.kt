package app.threedollars.data.response

import app.threedollars.domain.dto.CouponStatus
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class CouponResponseTest {

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    private fun fixture(name: String): String =
        checkNotNull(javaClass.classLoader?.getResourceAsStream(name)) { "fixture not found: $name" }
            .bufferedReader().use { it.readText() }

    // TH-718 TC9
    @Test
    fun `TH718_TC9_쿠폰_응답이_쿠폰명_기간_수량_상태로_매핑된다`() {
        // Given
        val response = json.decodeFromString<CouponListResponse>(fixture("couponList.json"))
        // When
        val dto = response.toDto()
        // Then
        val first = dto.contents.first()
        assertEquals("coupon-1", first.couponId)
        assertEquals("1000원 할인", first.name)
        assertEquals(999, first.maxIssuableCount)
        assertEquals(12, first.currentIssuedCount)
        assertEquals(3, first.currentUsedCount)
        assertEquals("2026-09-27T00:00:00", first.validityPeriod.startDateTime)
        assertEquals("2026-10-27T23:59:59", first.validityPeriod.endDateTime)
        assertEquals(CouponStatus.ACTIVE, first.status)
        assertEquals(false, dto.cursor?.hasMore)
    }

    // TH-718 TC9
    @Test
    fun `TH718_TC9_알_수_없는_status_값은_UNKNOWN으로_떨어져_크래시가_없다`() {
        // Given
        val response = json.decodeFromString<CouponListResponse>(fixture("couponList.json"))
        // When
        val second = response.toDto().contents[1]
        // Then
        assertEquals(CouponStatus.UNKNOWN, second.status)
    }
}
