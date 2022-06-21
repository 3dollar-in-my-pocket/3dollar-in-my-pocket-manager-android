package app.threedollars.data.store.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpeningHoursModel(
    @Json(name = "endTime")
    val endTime: String?,
    @Json(name = "startTime")
    val startTime: String?
)