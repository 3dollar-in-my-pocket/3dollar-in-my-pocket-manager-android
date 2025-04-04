package app.threedollars.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewCommentRequest(
    @SerialName("content")
    val content: String,
)