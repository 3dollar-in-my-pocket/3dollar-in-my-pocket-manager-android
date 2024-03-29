package app.threedollars.domain.dto


data class CategoriesDto(
    val category: String? = null,
    val categoryId: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val isNew: Boolean? = null,
    val name: String? = null
)