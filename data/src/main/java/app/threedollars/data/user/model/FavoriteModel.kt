package app.threedollars.data.user.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FavoriteModel(
    @Json(name = "isFavorite")
    val isFavorite: Boolean? = null
)