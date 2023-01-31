package app.threedollars.domain.dto


data class ContentsDto(
    val date: String? = null,
    val feedbacks: List<FeedbacksDto> = listOf()
)