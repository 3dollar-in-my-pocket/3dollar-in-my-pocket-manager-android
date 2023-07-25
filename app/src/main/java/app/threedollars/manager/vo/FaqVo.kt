package app.threedollars.manager.vo


data class FaqVo(
    val answer: String = "",
    val categoryInfo: CategoryInfoVo = CategoryInfoVo(),
    val question: String = ""
)