package app.threedollars.data.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class NonceRequest(
    @SerialName("retention")
    val retention: String = "PT1H",
)