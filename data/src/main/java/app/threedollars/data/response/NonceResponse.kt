package app.threedollars.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class NonceResponse(
    @SerialName("nonce")
    val nonce: String = "",
)