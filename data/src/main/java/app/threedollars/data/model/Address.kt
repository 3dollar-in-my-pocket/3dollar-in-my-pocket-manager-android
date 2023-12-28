package app.threedollars.data.model

import app.threedollars.domain.dto.AddressDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Address(
    @Json(name = "fullAddress")
    val fullAddress: String? = "",
) {
    fun toDto() = AddressDto(fullAddress ?: "")
}
