package app.threedollars.dto


data class FaqDto(
    val answer: String? = null,
    val category: String? = null,
    val categoryInfo: CategoryInfoDto? = null,
    val createdAt: String? = null,
    val faqId: Int? = null,
    val question: String? = null,
    val updatedAt: String? = null
)