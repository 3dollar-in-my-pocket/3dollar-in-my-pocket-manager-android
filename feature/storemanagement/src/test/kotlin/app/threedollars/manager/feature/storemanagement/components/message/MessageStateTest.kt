package app.threedollars.manager.feature.storemanagement.components.message

import app.threedollars.domain.dto.StoreMessageCreateDto
import app.threedollars.domain.dto.StoreMessageDto
import app.threedollars.domain.dto.StoreMessagePolicyDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class MessageStateTest {

    private val now = LocalDateTime.of(2024, 10, 24, 18, 0, 0)
    private val sendablePolicy = StoreMessagePolicyDto(canSendNow = true, nextAvailableSendDateTime = "2024-10-24T18:00:00")

    private fun message(id: String) = StoreMessageDto(messageId = id, body = "메세지$id", createdAt = "2024-10-24T10:00:00")

    private fun loaded(subscriberCount: Int, messages: List<StoreMessageDto>, hasMore: Boolean = false, nextCursor: String? = null) =
        MessageState(storeId = "store").withFirstPage(
            storeName = "가게",
            subscriberCount = subscriberCount,
            page = messages,
            nextCursor = nextCursor,
            hasMore = hasMore,
            policy = sendablePolicy,
        )

    // TH-391 TC1
    @Test
    fun `TH391_TC1_북마크_0명이고_이력이_없으면_북마크_요청_화면이고_전송_버튼은_비활성이다`() {
        // Given
        val state = loaded(subscriberCount = 0, messages = listOf())
        // When
        val screenType = state.screenType
        val buttonState = state.sendButtonState(now)
        // Then
        assertEquals(MessageScreenType.NO_BOOKMARK, screenType)
        assertEquals(MessageSendButtonState.Disabled, buttonState)
    }

    // TH-391 TC2
    @Test
    fun `TH391_TC2_북마크_1명_이상이고_이력이_없으면_오버뷰와_소개_화면이다`() {
        // Given
        val state = loaded(subscriberCount = 3, messages = listOf())
        // When
        val screenType = state.screenType
        // Then
        assertEquals(MessageScreenType.NO_HISTORY, screenType)
    }

    // TH-391 TC3
    @Test
    fun `TH391_TC3_전송_이력이_있으면_이전_메세지_목록_화면이다`() {
        // Given
        val state = loaded(subscriberCount = 3, messages = listOf(message("2"), message("1")))
        // When
        val screenType = state.screenType
        // Then
        assertEquals(MessageScreenType.HISTORY, screenType)
        assertEquals(listOf("2", "1"), state.messages.map { it.messageId })
    }

    // TH-391 TC5
    @Test
    fun `TH391_TC5_hasMore가_true고_커서가_있으면_다음_페이지를_이어_붙인다`() {
        // Given
        val state = loaded(subscriberCount = 3, messages = listOf(message("3"), message("2")), hasMore = true, nextCursor = "c1")
        assertTrue(state.canLoadMore)
        // When
        val next = state.withNextPage(page = listOf(message("1")), nextCursor = null, hasMore = false)
        // Then
        assertEquals(listOf("3", "2", "1"), next.messages.map { it.messageId })
        assertFalse(next.canLoadMore)
    }

    // TH-391 TC5
    @Test
    fun `TH391_TC5_hasMore가_false면_추가_조회하지_않는다`() {
        // Given
        val state = loaded(subscriberCount = 3, messages = listOf(message("1")), hasMore = false, nextCursor = "c1")
        // When
        val canLoadMore = state.canLoadMore
        // Then
        assertFalse(canLoadMore)
    }

    // TH-391 TC6
    @Test
    fun `TH391_TC6_새로고침_결과로_북마크_수_목록_정책을_갱신하고_인디케이터를_끈다`() {
        // Given
        val state = loaded(subscriberCount = 0, messages = listOf()).copy(isRefreshing = true)
        val policy = StoreMessagePolicyDto(canSendNow = false, nextAvailableSendDateTime = "2024-10-25T10:00:00")
        // When
        val refreshed = state.withFirstPage(
            storeName = "가게",
            subscriberCount = 5,
            page = listOf(message("1")),
            nextCursor = null,
            hasMore = false,
            policy = policy,
        )
        // Then
        assertFalse(refreshed.isRefreshing)
        assertEquals(5, refreshed.subscriberCount)
        assertEquals(listOf("1"), refreshed.messages.map { it.messageId })
        assertEquals(policy, refreshed.policy)
    }

    // TH-391 TC7
    @Test
    fun `TH391_TC7_canSendNow가_true면_전송_버튼이_활성이다`() {
        // Given
        val state = loaded(subscriberCount = 1, messages = listOf())
        // When
        val buttonState = state.sendButtonState(now)
        // Then
        assertEquals(MessageSendButtonState.Enabled, buttonState)
    }

    // TH-391 TC8
    @Test
    fun `TH391_TC8_canSendNow가_false면_다음_발송_가능_시각까지_카운트다운한다`() {
        // Given
        val policy = StoreMessagePolicyDto(canSendNow = false, nextAvailableSendDateTime = "2024-10-24T20:45:59")
        // When
        val first = MessageSendButtonState.of(subscriberCount = 1, policy = policy, now = now)
        val oneSecondLater = MessageSendButtonState.of(subscriberCount = 1, policy = policy, now = now.plusSeconds(1))
        // Then
        assertEquals(MessageSendButtonState.Countdown("02:45:59"), first)
        assertEquals(MessageSendButtonState.Countdown("02:45:58"), oneSecondLater)
    }

    // TH-391 TC8
    @Test
    fun `TH391_TC8_다음_발송_가능_시각에_도달하면_전송_버튼이_활성으로_바뀐다`() {
        // Given
        val policy = StoreMessagePolicyDto(canSendNow = false, nextAvailableSendDateTime = "2024-10-24T20:45:59")
        // When
        val reached = MessageSendButtonState.of(subscriberCount = 1, policy = policy, now = LocalDateTime.of(2024, 10, 24, 20, 45, 59))
        // Then
        assertEquals(MessageSendButtonState.Enabled, reached)
    }

    // TH-391 TC16
    @Test
    fun `TH391_TC16_전송_성공하면_새_메세지를_목록_최상단에_추가하고_응답_정책으로_카운트다운을_시작한다`() {
        // Given
        val state = loaded(subscriberCount = 3, messages = listOf(message("1"))).copy(confirm = MessageConfirmState(body = "새 메세지입니다 열 글자", nonce = "n"))
        val policy = StoreMessagePolicyDto(canSendNow = false, nextAvailableSendDateTime = "2024-10-25T18:00:00")
        val created = StoreMessageCreateDto(message = message("2"), policy = policy)
        // When
        val sent = state.withMessageSent(created)
        // Then
        assertEquals(listOf("2", "1"), sent.messages.map { it.messageId })
        assertNull(sent.confirm)
        assertEquals(MessageSendButtonState.Countdown("24:00:00"), sent.sendButtonState(now))
    }

    // TH-391 TC17
    @Test
    fun `TH391_TC17_이력이_없던_상태에서_전송_성공하면_이전_메세지_목록_화면으로_바뀐다`() {
        // Given
        val state = loaded(subscriberCount = 3, messages = listOf())
        assertEquals(MessageScreenType.NO_HISTORY, state.screenType)
        // When
        val sent = state.withMessageSent(StoreMessageCreateDto(message = message("1"), policy = sendablePolicy))
        // Then
        assertEquals(MessageScreenType.HISTORY, sent.screenType)
    }
}
