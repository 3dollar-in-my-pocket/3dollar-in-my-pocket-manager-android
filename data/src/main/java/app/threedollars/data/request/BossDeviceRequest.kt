package app.threedollars.data.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BossDeviceRequest(
    @Json(name = "pushPlatformType")
    val pushPlatformType: String? = null,
    @Json(name = "pushToken")
    val pushToken: String? = null
)