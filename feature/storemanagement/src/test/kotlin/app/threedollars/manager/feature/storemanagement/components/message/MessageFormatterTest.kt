package app.threedollars.manager.feature.storemanagement.components.message

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Duration
import java.time.LocalDateTime

class MessageFormatterTest {

    // TH-391 TC4
    @Test
    fun `TH391_TC4_전송_24시간_이내면_HH_mm으로_표시한다`() {
        // Given
        val now = LocalDateTime.of(2024, 10, 24, 21, 0, 0)
        val createdAt = "2024-10-23T21:00:01"
        // When
        val text = MessageFormatter.formatSentAt(createdAt, now)
        // Then
        assertEquals("21:00", text)
    }

    // TH-391 TC4
    @Test
    fun `TH391_TC4_전송_24시간이_지나면_yyyy_MM_dd_요일로_표시한다`() {
        // Given
        val now = LocalDateTime.of(2024, 10, 24, 21, 0, 0)
        val createdAt = "2024-10-23T21:00:00"
        // When
        val text = MessageFormatter.formatSentAt(createdAt, now)
        // Then
        assertEquals("2024.10.23 수", text)
    }

    // TH-391 TC4
    @Test
    fun `TH391_TC4_시각을_해석할_수_없으면_빈_문자열을_돌려준다`() {
        // Given
        val createdAt = "invalid"
        // When
        val text = MessageFormatter.formatSentAt(createdAt, LocalDateTime.of(2024, 10, 24, 21, 0, 0))
        // Then
        assertEquals("", text)
    }

    // TH-391 TC8
    @Test
    fun `TH391_TC8_남은_시간을_HH_mm_ss로_표시한다`() {
        // Given
        val remaining = Duration.ofHours(2).plusMinutes(45).plusSeconds(59)
        // When
        val text = MessageFormatter.formatRemaining(remaining)
        // Then
        assertEquals("02:45:59", text)
    }
}
