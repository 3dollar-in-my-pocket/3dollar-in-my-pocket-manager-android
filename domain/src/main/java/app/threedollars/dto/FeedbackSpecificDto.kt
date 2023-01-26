package app.threedollars.dto


data class FeedbackSpecificDto(
    val contents: List<ContentsDto>? = null,
    val cursor: CursorDto? = null,
)
