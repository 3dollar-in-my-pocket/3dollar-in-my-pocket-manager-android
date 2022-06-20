package app.threedollars.data.store.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryModel(
    @Json(name = "categoryId")
    val categoryId: String?,
    @Json(name = "name")
    val name: String?
)