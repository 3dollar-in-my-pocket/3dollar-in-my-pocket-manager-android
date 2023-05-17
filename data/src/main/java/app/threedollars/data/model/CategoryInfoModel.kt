package app.threedollars.data.model


import app.threedollars.domain.dto.CategoryInfoDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryInfoModel(
    @Json(name = "category")
    val category: String = "",
    @Json(name = "description")
    val description: String = "",
    @Json(name = "displayOrder")
    val displayOrder: String = ""
){
    fun toDto() = CategoryInfoDto(category, description, displayOrder)

}