package app.threedollars.data.response

import app.threedollars.domain.dto.CommentCreateDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentCreateResponse(
    @SerialName("commentId")
    val commentId: String = ""
) {
    fun toDto() = CommentCreateDto(
        commentId = commentId
    )
}
