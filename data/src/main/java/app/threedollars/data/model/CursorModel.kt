package app.threedollars.data.model


import app.threedollars.domain.dto.CursorDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CursorModel(
    @Json(name = "hasMore")
    val hasMore: Boolean = false,
    @Json(name = "nextCursor")
    val nextCursor: String? = null
){
    fun toDto() = CursorDto(hasMore, nextCursor)
}