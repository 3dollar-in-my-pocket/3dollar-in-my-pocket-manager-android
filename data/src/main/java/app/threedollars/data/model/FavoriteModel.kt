package app.threedollars.data.model


import app.threedollars.domain.dto.FavoriteDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FavoriteModel(
    @Json(name = "isFavorite")
    val isFavorite: Boolean? = null
){
    fun toDto() = FavoriteDto(isFavorite)

}