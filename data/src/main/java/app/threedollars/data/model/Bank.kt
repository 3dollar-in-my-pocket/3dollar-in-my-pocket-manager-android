package app.threedollars.data.model

import app.threedollars.domain.dto.BankDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Bank(
    @Json(name = "key")
    val key: String? = "",
    @Json(name = "description")
    val description: String? = "",
) {
    fun toDto() = BankDto(key = key ?: "", description = description ?: "")
}
