package app.threedollars.domain.dto


data class CursorDto(
    val hasMore: Boolean = false,
    val nextCursor: String? = null,
    val totalCount: Int? = null
)