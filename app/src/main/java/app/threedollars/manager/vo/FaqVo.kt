package app.threedollars.manager.vo


data class FaqVo(
    val answer: String? = null,
    val category: String? = null,
    val categoryInfo: CategoryInfoVo? = null,
    val createdAt: String? = null,
    val faqId: Int? = null,
    val question: String? = null,
    val updatedAt: String? = null
)