package app.threedollars.data.model


import app.threedollars.domain.dto.EnumsDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EnumsModel(
    @Json(name = "category")
    val category: String? = null,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "displayOrder")
    val displayOrder: String? = null
)
fun List<EnumsModel>.toDto() = map {
    EnumsDto(it.category, it.description, it.displayOrder)
}