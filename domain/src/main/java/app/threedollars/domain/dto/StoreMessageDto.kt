package app.threedollars.domain.dto

data class StoreMessageListDto(
    val contents: List<StoreMessageDto> = listOf(),
    val cursor: CursorDto? = CursorDto(),
    val policy: StoreMessagePolicyDto = StoreMessagePolicyDto(),
)

data class StoreMessageDto(
    val messageId: String = "",
    val body: String = "",
    val isOwner: Boolean = false,
    val createdAt: String = "",
    val updatedAt: String = "",
)

data class StoreMessagePolicyDto(
    val canSendNow: Boolean = false,
    val nextAvailableSendDateTime: String = "",
    val maxQuota: Long = 0,
    val remainingQuota: Long = 0,
)

data class StoreMessageCreateDto(
    val message: StoreMessageDto = StoreMessageDto(),
    val policy: StoreMessagePolicyDto = StoreMessagePolicyDto(),
)
