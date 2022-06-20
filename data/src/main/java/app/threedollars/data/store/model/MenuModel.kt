package app.threedollars.data.store.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MenuModel(
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "price")
    val price: Int?
)