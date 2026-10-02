package app.threedollars.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StoreMessageCreateRequest(
    @SerialName("body")
    val body: String,
)
