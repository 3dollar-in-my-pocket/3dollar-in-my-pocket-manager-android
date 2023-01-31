package app.threedollars.data.model


import app.threedollars.domain.dto.LocationDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationModel(
    @Json(name = "latitude")
    val latitude: Double? = null,
    @Json(name = "longitude")
    val longitude: Double? = null
){
    fun toDto() = LocationDto(latitude, longitude)

}