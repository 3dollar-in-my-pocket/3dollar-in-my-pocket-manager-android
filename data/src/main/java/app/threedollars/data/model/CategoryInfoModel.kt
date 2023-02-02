package app.threedollars.data.model


import app.threedollars.domain.dto.CategoryInfoDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryInfoModel(
    @Json(name = "category")
    val category: String? = null,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "displayOrder")
    val displayOrder: String? = null
){
    fun toDto() = CategoryInfoDto(category, description, displayOrder)

}