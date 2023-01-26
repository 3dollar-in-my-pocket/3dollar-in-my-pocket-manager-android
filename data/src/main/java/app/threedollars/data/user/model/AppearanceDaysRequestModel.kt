package app.threedollars.data.user.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppearanceDaysRequestModel(
    @Json(name = "dayOfTheWeek")
    val dayOfTheWeek: String? = null,
    @Json(name = "startTime")
    val startTime: String? = null,
    @Json(name = "endTime")
    val endTime: String? = null,
    @Json(name = "locationDescription")
    val locationDescription: String? = null
)