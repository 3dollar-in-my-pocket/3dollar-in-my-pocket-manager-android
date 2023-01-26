package app.threedollars.data.user.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CursorModel(
    @Json(name = "hasMore")
    val hasMore: Boolean? = null,
    @Json(name = "nextCursor")
    val nextCursor: String? = null
)