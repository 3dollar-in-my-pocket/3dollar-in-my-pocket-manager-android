package app.threedollars.manager.vo


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoriesVo(
    val category: String? = null,
    val categoryId: String? = null,
    val description: String? = null,
    val imageUrl: String? = null,
    val isNew: Boolean? = null,
    val name: String? = null
)