package app.threedollars.data.response


import app.threedollars.domain.dto.StoreCategoriesDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoreCategoriesResponse(
    @Json(name = "category")
    val category: String = "",
    @Json(name = "categoryId")
    val categoryId: String = "",
    @Json(name = "description")
    val description: String = "",
    @Json(name = "imageUrl")
    val imageUrl: String = "",
    @Json(name = "isNew")
    val isNew: Boolean = false,
    @Json(name = "name")
    val name: String = ""
)

fun List<StoreCategoriesResponse>.toDto() = map {
    StoreCategoriesDto(it.category, it.categoryId, it.description, it.imageUrl, it.isNew, it.name)
}
