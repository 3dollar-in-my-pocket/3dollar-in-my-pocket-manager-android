package app.threedollars.data.model


import app.threedollars.domain.dto.OpeningHoursDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpeningHoursModel(
    @Json(name = "startTime")
    val startTime: String? = null,
    @Json(name = "endTime")
    val endTime: String? = null
){
    fun toDto() = OpeningHoursDto(startTime, endTime)
}