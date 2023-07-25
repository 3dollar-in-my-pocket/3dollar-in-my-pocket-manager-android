package app.threedollars.data.model


import app.threedollars.domain.dto.CategoriesDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoriesModel(
    @Json(name = "category")
    val category: String? = null,
    @Json(name = "categoryId")
    val categoryId: String? = null,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "imageUrl")
    val imageUrl: String? = null,
    @Json(name = "isNew")
    val isNew: Boolean? = null,
    @Json(name = "name")
    val name: String? = null
)

fun List<CategoriesModel>.toDto() = map {
    CategoriesDto(it.category, it.categoryId, it.description, it.imageUrl, it.isNew, it.name)
}
