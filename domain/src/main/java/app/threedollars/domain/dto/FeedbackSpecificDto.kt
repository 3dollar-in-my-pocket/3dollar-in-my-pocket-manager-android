package app.threedollars.domain.dto


data class FeedbackSpecificDto(
    val contents: List<ContentsDto> = listOf(),
    val cursor: CursorDto = CursorDto(),
)
