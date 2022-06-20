package app.threedollars.data.store.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppearanceDayModel(
    @Json(name = "dayOfTheWeek")
    val dayOfTheWeek: String?,
    @Json(name = "locationDescription")
    val locationDescription: String?,
    @Json(name = "openingHours")
    val openingHoursModel: OpeningHoursModel?
)