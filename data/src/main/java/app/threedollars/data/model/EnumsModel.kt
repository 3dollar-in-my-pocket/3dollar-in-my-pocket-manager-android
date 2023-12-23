package app.threedollars.data.model


import app.threedollars.domain.dto.EnumsDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EnumsModel(
    @Json(name = "key")
    val key: String? = null,
    @Json(name = "description")
    val description: String? = null,
)
fun List<EnumsModel>.toDto() = map {
    EnumsDto(it.key, it.description)
}