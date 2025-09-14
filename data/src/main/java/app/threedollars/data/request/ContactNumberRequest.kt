package app.threedollars.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContactNumberRequest(
    @SerialName("number")
    val number: String,
    @SerialName("description")
    val description: String? = null,
)