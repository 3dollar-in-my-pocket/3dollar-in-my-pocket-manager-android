package app.threedollars.domain.dto


data class FaqDto(
    val answer: String = "",
    val category: String = "",
    val categoryInfo: CategoryInfoDto = CategoryInfoDto(),
    val createdAt: String = "",
    val faqId: Int = 0,
    val question: String = "",
    val updatedAt: String = ""
)