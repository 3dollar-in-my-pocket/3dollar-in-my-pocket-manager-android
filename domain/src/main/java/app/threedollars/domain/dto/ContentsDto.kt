package app.threedollars.domain.dto


data class ContentsDto(
    val date: String = "",
    val feedbacks: List<FeedbacksDto> = listOf()
)