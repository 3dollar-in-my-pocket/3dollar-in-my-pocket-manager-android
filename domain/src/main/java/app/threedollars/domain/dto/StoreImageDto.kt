package app.threedollars.domain.dto

data class StoreImageDto(
    val imageUrl: String,
    val width: Int? = null,
    val height: Int? = null,
)
