package app.threedollars.manager.vo


data class ContentsVo(
    val date: String? = null,
    val feedbacks: List<FeedbacksVo> = listOf()
)