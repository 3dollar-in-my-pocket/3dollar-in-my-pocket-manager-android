package app.threedollars.data.response

import app.threedollars.common.ext.toStringDefault
import app.threedollars.data.model.CursorModel
import app.threedollars.domain.dto.CommentPresetDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentPresetResponse(
    @SerialName("contents")
    val contents: List<CommentPreset> = listOf(),
    @SerialName("cursor")
    val cursor: CursorModel
) {
    @Serializable
    data class CommentPreset(
        @SerialName("presetId")
        val presetId: String,
        @SerialName("body")
        val body: String,
        @SerialName("createdAt")
        val createdAt: String? = null,
        @SerialName("updateAt")
        val updateAt: String? = null
    ) {
        fun toDto() = CommentPresetDto.CommentPreset(
            presetId = presetId,
            body = body,
            createdAt = createdAt.toStringDefault(),
            updateAt = updateAt.toStringDefault()
        )
    }

    fun toDto() = CommentPresetDto(
        contents = contents.map { it.toDto() }
    )
}
