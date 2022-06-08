package app.threedollars.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponse<T>
    (
    @Json(name = "data")
    val data: T? = null,
    @Json(name = "message")
    val message: String? = "",
    @Json(name = "resultCode")
    val resultCode: String? = ""
)