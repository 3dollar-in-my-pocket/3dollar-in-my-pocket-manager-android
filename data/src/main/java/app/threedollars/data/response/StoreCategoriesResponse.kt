package app.threedollars.data.response


import app.threedollars.dto.LoginDto
import app.threedollars.dto.StoreCategoriesDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoreCategoriesResponse(
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
) {
    fun toDto() = StoreCategoriesDto(category, categoryId, description, imageUrl, isNew, name)

}