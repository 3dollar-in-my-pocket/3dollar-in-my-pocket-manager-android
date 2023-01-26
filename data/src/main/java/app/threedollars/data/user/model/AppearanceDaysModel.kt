package app.threedollars.data.user.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppearanceDaysModel(
    @Json(name = "dayOfTheWeek")
    val dayOfTheWeek: String? = null,
    @Json(name = "endTime")
    val openingHours: OpeningHoursModel? = null,
    @Json(name = "locationDescription")
    val locationDescription: String? = null
)