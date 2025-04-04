package app.threedollars.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentPresetRequest(
    @SerialName("body")
    val body: String,
)