package app.threedollars.domain.dto


data class CursorDto(
    val hasMore: Boolean? = null,
    val nextCursor: String? = null
)