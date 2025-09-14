package app.threedollars.data.model

import app.threedollars.domain.dto.ContactNumbersDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContactNumbers(
    @SerialName("number")
    val number: String = "",
    @SerialName("description")
    val description: String? = null,
) {
    fun toDto() = ContactNumbersDto(
        number = number,
        description = description,
    )
}