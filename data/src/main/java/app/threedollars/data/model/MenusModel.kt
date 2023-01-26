package app.threedollars.data.model


import app.threedollars.dto.MenusDto
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

fun List<MenusModel>.toDto() = map {
    MenusDto(it.imageUrl, it.name, it.price)
}
