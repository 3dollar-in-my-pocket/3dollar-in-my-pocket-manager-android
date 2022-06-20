package app.threedollars.data.store.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TimeModel(
    @Json(name = "hour")
    val hour: Int?,
    @Json(name = "minute")
    val minute: Int?,
    @Json(name = "nano")
    val nano: Int?,
    @Json(name = "second")
    val second: Int?
)