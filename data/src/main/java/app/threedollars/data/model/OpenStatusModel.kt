package app.threedollars.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenStatusModel(
    @Json(name = "openStartDateTime")
    val openStartDateTime: String? = null,
    @Json(name = "status")
    val status: String? = null,
)