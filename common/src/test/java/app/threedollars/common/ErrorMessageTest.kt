package app.threedollars.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ErrorMessageTest {

    // TH-718 TC20
    @Test
    fun `TH718_TC20_서버_에러_JSON이면_message만_꺼내_보여준다`() {
        // Given
        val raw = """{"ok":false,"resultCode":"BR001","error":"bad_request","message":"사용 기간이 올바르지 않습니다"}"""
        // When
        val message = raw.toDisplayErrorMessage()
        // Then
        assertEquals("사용 기간이 올바르지 않습니다", message)
    }

    // TH-718 TC20
    @Test
    fun `TH718_TC20_JSON이_아닌_에러는_원문을_그대로_보여준다`() {
        // Given
        val raw = "Unable to resolve host \"dev.threedollars.co.kr\""
        // When
        val message = raw.toDisplayErrorMessage()
        // Then
        assertEquals(raw, message)
    }

    // TH-718 TC20
    @Test
    fun `TH718_TC20_message가_없는_JSON이나_빈_값은_null로_기본_문구를_쓰게_한다`() {
        // Given
        val noMessage = """{"ok":false,"resultCode":"E500"}"""
        // When / Then
        assertNull(noMessage.toDisplayErrorMessage())
        assertNull("".toDisplayErrorMessage())
        assertNull(null.toDisplayErrorMessage())
    }
}
