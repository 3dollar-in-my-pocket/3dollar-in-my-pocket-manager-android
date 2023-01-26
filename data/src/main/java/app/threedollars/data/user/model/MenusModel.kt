package app.threedollars.data.user.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MenusModel(
    @Json(name = "imageUrl")
    val imageUrl: String? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "price")
    val price: Int? = null
)