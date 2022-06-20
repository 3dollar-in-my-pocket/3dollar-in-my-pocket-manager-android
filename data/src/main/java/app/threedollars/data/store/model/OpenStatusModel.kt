package app.threedollars.data.store.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenStatusModel(
    @Json(name = "openStartDateTime")
    val openStartDateTime: Any?,
    @Json(name = "status")
    val status: String?
)