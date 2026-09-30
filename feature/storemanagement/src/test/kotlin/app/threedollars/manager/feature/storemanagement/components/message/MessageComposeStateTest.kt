package app.threedollars.manager.feature.storemanagement.components.message

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MessageComposeStateTest {

    // TH-391 TC9
    @Test
    fun `TH391_TC9_작성_시트는_빈_입력_기본_상태로_열린다`() {
        // Given
        val compose = MessageComposeState()
        // When
        val length = compose.body.length
        // Then
        assertEquals(0, length)
        assertEquals(MessageInputState.NORMAL, compose.inputState)
    }

    // TH-391 TC10
    @Test
    fun `TH391_TC10_입력창에_포커스되면_포커스_상태가_된다`() {
        // Given
        val compose = MessageComposeState()
        // When
        val focused = compose.withFocusChanged(focused = true)
        // Then
        assertEquals(MessageInputState.FOCUSED, focused.inputState)
    }

    // TH-391 TC10
    @Test
    fun `TH391_TC10_100자를_넘는_입력은_붙여넣기여도_100자로_잘린다`() {
        // Given
        val compose = MessageComposeState()
        val pasted = "가".repeat(150)
        // When
        val updated = compose.withBody(pasted)
        // Then
        assertEquals(MESSAGE_MAX_LENGTH, updated.body.length)
    }

    // TH-391 TC11
    @Test
    fun `TH391_TC11_10자_미만이면_유효하지_않고_에러_상태가_된다`() {
        // Given
        val compose = MessageComposeState(body = "가".repeat(9), inputState = MessageInputState.FOCUSED)
        // When
        val validated = if (compose.isValid) compose else compose.withValidationError()
        // Then
        assertFalse(compose.isValid)
        assertEquals(MessageInputState.ERROR, validated.inputState)
    }

    // TH-391 TC11
    @Test
    fun `TH391_TC11_에러_상태에서_포커스가_빠져도_에러_상태를_유지한다`() {
        // Given
        val compose = MessageComposeState(body = "짧음", inputState = MessageInputState.ERROR)
        // When
        val blurred = compose.withFocusChanged(focused = false)
        // Then
        assertEquals(MessageInputState.ERROR, blurred.inputState)
    }

    // TH-391 TC12
    @Test
    fun `TH391_TC12_에러_상태에서_입력창을_터치하면_포커스_상태로_돌아간다`() {
        // Given
        val compose = MessageComposeState(body = "짧음", inputState = MessageInputState.ERROR)
        // When
        val touched = compose.withTouched()
        // Then
        assertEquals(MessageInputState.FOCUSED, touched.inputState)
    }

    // TH-391 TC13
    @Test
    fun `TH391_TC13_10자에서_100자_사이면_유효하다`() {
        // Given
        val min = MessageComposeState(body = "가".repeat(MESSAGE_MIN_LENGTH))
        val max = MessageComposeState(body = "가".repeat(MESSAGE_MAX_LENGTH))
        // When
        val results = listOf(min.isValid, max.isValid)
        // Then
        assertEquals(listOf(true, true), results)
    }

    // TH-391 TC18
    @Test
    fun `TH391_TC18_nonce_발급_전이거나_실패하면_전송할_수_없다`() {
        // Given
        val confirm = MessageConfirmState(body = "가".repeat(MESSAGE_MIN_LENGTH), nonce = null)
        // When
        val canSend = confirm.canSend
        // Then
        assertFalse(canSend)
    }

    // TH-391 TC18
    @Test
    fun `TH391_TC18_nonce가_있으면_전송할_수_있고_전송_중에는_중복_전송하지_않는다`() {
        // Given
        val confirm = MessageConfirmState(body = "가".repeat(MESSAGE_MIN_LENGTH), nonce = "nonce")
        // When
        val sending = confirm.copy(isSending = true)
        // Then
        assertTrue(confirm.canSend)
        assertFalse(sending.canSend)
    }
}
