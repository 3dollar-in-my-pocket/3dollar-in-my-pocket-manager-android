package app.threedollars.data.response

import app.threedollars.common.ext.toStringDefault
import app.threedollars.data.model.CursorModel
import app.threedollars.domain.dto.StoreMessageCreateDto
import app.threedollars.domain.dto.StoreMessageDto
import app.threedollars.domain.dto.StoreMessageListDto
import app.threedollars.domain.dto.StoreMessagePolicyDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StoreMessageListResponse(
    @SerialName("messages")
    val messages: Messages? = null,
    @SerialName("policy")
    val policy: StoreMessagePolicyResponse? = null,
) {
    @Serializable
    internal data class Messages(
        @SerialName("contents")
        val contents: List<StoreMessageResponse> = listOf(),
        @SerialName("cursor")
        val cursor: CursorModel? = CursorModel(),
    )

    fun toDto() = StoreMessageListDto(
        contents = messages?.contents.orEmpty().map { it.toDto() },
        cursor = messages?.cursor?.toDto(),
        policy = policy?.toDto() ?: StoreMessagePolicyDto(),
    )
}

@Serializable
internal data class StoreMessageCreateResponse(
    @SerialName("message")
    val message: StoreMessageResponse? = null,
    @SerialName("policy")
    val policy: StoreMessagePolicyResponse? = null,
) {
    fun toDto() = StoreMessageCreateDto(
        message = message?.toDto() ?: StoreMessageDto(),
        policy = policy?.toDto() ?: StoreMessagePolicyDto(),
    )
}

@Serializable
internal data class StoreMessageResponse(
    @SerialName("messageId")
    val messageId: String = "",
    @SerialName("body")
    val body: String? = null,
    @SerialName("isOwner")
    val isOwner: Boolean = false,
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("updatedAt")
    val updatedAt: String? = null,
) {
    fun toDto() = StoreMessageDto(
        messageId = messageId,
        body = body.toStringDefault(),
        isOwner = isOwner,
        createdAt = createdAt.toStringDefault(),
        updatedAt = updatedAt.toStringDefault(),
    )
}

@Serializable
internal data class StoreMessagePolicyResponse(
    @SerialName("canSendNow")
    val canSendNow: Boolean = false,
    @SerialName("nextAvailableSendDateTime")
    val nextAvailableSendDateTime: String? = null,
    @SerialName("maxQuota")
    val maxQuota: Long = 0,
    @SerialName("remainingQuota")
    val remainingQuota: Long = 0,
) {
    fun toDto() = StoreMessagePolicyDto(
        canSendNow = canSendNow,
        nextAvailableSendDateTime = nextAvailableSendDateTime.toStringDefault(),
        maxQuota = maxQuota,
        remainingQuota = remainingQuota,
    )
}
