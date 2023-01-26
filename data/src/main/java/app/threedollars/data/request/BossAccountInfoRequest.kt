package app.threedollars.data.request


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BossAccountInfoRequest(
    @Json(name = "name")
    val name: String? = null,
)