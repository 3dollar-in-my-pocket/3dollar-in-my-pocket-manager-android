package app.threedollars.data.store.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Category(
    @Json(name = "categoryId")
    val categoryId: String?,
    @Json(name = "name")
    val name: String?
)