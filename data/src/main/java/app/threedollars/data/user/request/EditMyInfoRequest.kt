package app.threedollars.data.user.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EditMyInfoRequest(
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "pushSettingsStatus")
    val pushSettingsStatus: String? = null
)