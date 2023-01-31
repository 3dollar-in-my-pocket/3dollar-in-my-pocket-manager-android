package app.threedollars.data.response


import app.threedollars.domain.dto.FaqCategoriesDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FaqCategoriesResponse(
    @Json(name = "category")
    val category: String? = null,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "displayOrder")
    val displayOrder: Int? = null
)

fun List<FaqCategoriesResponse>.toDto() = map {
    FaqCategoriesDto(it.category, it.description, it.displayOrder)
}
