package app.threedollars.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StickersReplaceRequest(
    @SerialName("stickers")
    val stickers: List<Sticker>,
) {
    @Serializable
    data class Sticker(
        @SerialName("stickerId")
        val stickerId: String,
    )
}