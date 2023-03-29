package app.threedollars.domain.dto


data class StoreCategoriesDto(
    val category: String = "",
    val categoryId: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val isNew: Boolean = false,
    val name: String = ""
)