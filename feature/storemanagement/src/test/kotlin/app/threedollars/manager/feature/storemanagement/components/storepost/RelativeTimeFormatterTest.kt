package app.threedollars.manager.feature.storemanagement.components.storepost

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

class RelativeTimeFormatterTest {

    private val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    private val now = format.parse("2026-09-26T12:00:00")!!.time

    // TH-198 TC2
    @Test
    fun `TH198_TC2_생성_시각과의_차이에_따라_상대_시간을_표시한다`() {
        // Given
        val cases = mapOf(
            "2026-09-26T11:59:30" to "방금 전",
            "2026-09-26T11:45:00" to "15분 전",
            "2026-09-26T09:00:00" to "3시간 전",
            "2026-09-25T10:00:00" to "1일 전",
            "2026-09-20T10:00:00" to "2026년09월20일",
        )
        cases.forEach { (createdAt, expected) ->
            // When
            val text = RelativeTimeFormatter.format(createdAt, nowMillis = now)
            // Then
            assertEquals(expected, text)
        }
    }

    // TH-198 TC2
    @Test
    fun `TH198_TC2_파싱할_수_없는_날짜는_빈_문자열로_표시해_크래시가_없다`() {
        // Given
        val createdAt = "not-a-date"
        // When
        val text = RelativeTimeFormatter.format(createdAt, nowMillis = now)
        // Then
        assertEquals("", text)
    }
}
