package app.threedollars.data.store.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenStatus(
    @Json(name = "openStartDateTime")
    val openStartDateTime: Any?,
    @Json(name = "status")
    val status: String?
)