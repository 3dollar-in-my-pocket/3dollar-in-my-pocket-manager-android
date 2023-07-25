package app.threedollars.manager.vo


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoriesVo(
    val category: String,
    val categoryId: String,
    val description: String,
    val imageUrl: String,
    val isNew: Boolean,
    val name: String
)