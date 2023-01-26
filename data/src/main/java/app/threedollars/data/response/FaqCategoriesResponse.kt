package app.threedollars.data.response


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