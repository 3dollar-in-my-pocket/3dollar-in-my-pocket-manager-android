package app.threedollars.data.response


import app.threedollars.dto.BossAccountInfoDto
import app.threedollars.dto.FaqCategoriesDto
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
){
    fun toDto() = FaqCategoriesDto(category, description, displayOrder)

}