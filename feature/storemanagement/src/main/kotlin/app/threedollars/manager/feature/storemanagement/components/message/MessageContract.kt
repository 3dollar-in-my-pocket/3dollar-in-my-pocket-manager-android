package app.threedollars.manager.feature.storemanagement.components.message

import app.threedollars.domain.dto.StoreMessageCreateDto
import app.threedollars.domain.dto.StoreMessageDto
import app.threedollars.domain.dto.StoreMessagePolicyDto
import java.time.Duration
import java.time.LocalDateTime

internal const val MESSAGE_PAGE_SIZE = 20
internal const val MESSAGE_MIN_LENGTH = 10
internal const val MESSAGE_MAX_LENGTH = 100

internal enum class MessageScreenType {
    NO_BOOKMARK,
    NO_HISTORY,
    HISTORY,
}

internal sealed interface MessageSendButtonState {
    data object Disabled : MessageSendButtonState
    data class Countdown(val remaining: String) : MessageSendButtonState
    data object Enabled : MessageSendButtonState

    companion object {
        fun of(subscriberCount: Int, policy: StoreMessagePolicyDto?, now: LocalDateTime): MessageSendButtonState {
            if (subscriberCount <= 0 || policy == null) return Disabled
            if (policy.canSendNow) return Enabled
            val nextAvailable = MessageFormatter.parse(policy.nextAvailableSendDateTime) ?: return Disabled
            if (!now.isBefore(nextAvailable)) return Enabled
            return Countdown(MessageFormatter.formatRemaining(Duration.between(now, nextAvailable)))
        }
    }
}

internal enum class MessageInputState {
    NORMAL,
    FOCUSED,
    ERROR,
}

internal data class MessageComposeState(
    val body: String = "",
    val inputState: MessageInputState = MessageInputState.NORMAL,
) {
    val isValid: Boolean get() = body.length in MESSAGE_MIN_LENGTH..MESSAGE_MAX_LENGTH

    fun withBody(input: String) = copy(body = input.take(MESSAGE_MAX_LENGTH))

    fun withFocusChanged(focused: Boolean) = copy(
        inputState = when {
            focused -> MessageInputState.FOCUSED
            inputState == MessageInputState.ERROR -> MessageInputState.ERROR
            else -> MessageInputState.NORMAL
        },
    )

    fun withTouched() = copy(inputState = MessageInputState.FOCUSED)

    fun withValidationError() = copy(inputState = MessageInputState.ERROR)
}

internal data class MessageConfirmState(
    val body: String,
    val nonce: String? = null,
    val isSending: Boolean = false,
) {
    val canSend: Boolean get() = !nonce.isNullOrEmpty() && !isSending
}

internal data class MessageError(val message: String?)

internal data class MessageState(
    val storeId: String = "",
    val storeName: String = "",
    val subscriberCount: Int = 0,
    val messages: List<StoreMessageDto> = listOf(),
    val policy: StoreMessagePolicyDto? = null,
    val isInitialLoaded: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val nextCursor: String? = null,
    val hasMore: Boolean = false,
    val compose: MessageComposeState? = null,
    val confirm: MessageConfirmState? = null,
    val error: MessageError? = null,
) {
    val screenType: MessageScreenType
        get() = when {
            messages.isNotEmpty() -> MessageScreenType.HISTORY
            subscriberCount <= 0 -> MessageScreenType.NO_BOOKMARK
            else -> MessageScreenType.NO_HISTORY
        }

    val canLoadMore: Boolean
        get() = isInitialLoaded && hasMore && !nextCursor.isNullOrEmpty() && !isLoading && !isRefreshing && !isLoadingMore

    fun sendButtonState(now: LocalDateTime): MessageSendButtonState =
        MessageSendButtonState.of(subscriberCount = subscriberCount, policy = policy, now = now)

    fun withFirstPage(
        storeName: String,
        subscriberCount: Int,
        page: List<StoreMessageDto>,
        nextCursor: String?,
        hasMore: Boolean,
        policy: StoreMessagePolicyDto,
    ) = copy(
        storeName = storeName,
        subscriberCount = subscriberCount,
        messages = page,
        policy = policy,
        isInitialLoaded = true,
        isLoading = false,
        isRefreshing = false,
        isLoadingMore = false,
        nextCursor = nextCursor,
        hasMore = hasMore,
    )

    fun withNextPage(page: List<StoreMessageDto>, nextCursor: String?, hasMore: Boolean) = copy(
        messages = messages + page.filterNot { next -> messages.any { it.messageId == next.messageId } },
        isLoadingMore = false,
        nextCursor = nextCursor,
        hasMore = hasMore,
    )

    fun withMessageSent(created: StoreMessageCreateDto) = copy(
        messages = listOf(created.message) + messages.filterNot { it.messageId == created.message.messageId },
        policy = created.policy,
        confirm = null,
    )
}
