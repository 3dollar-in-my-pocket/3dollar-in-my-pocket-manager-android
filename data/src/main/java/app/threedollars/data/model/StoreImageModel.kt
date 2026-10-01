package app.threedollars.data.model

import app.threedollars.domain.dto.StoreImageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreImageModel(
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("width")
    val width: Int? = null,
    @SerialName("height")
    val height: Int? = null,
)

internal fun List<StoreImageModel>?.toRepresentativeImageDtos(fallbackImageUrl: String?): List<StoreImageDto> {
    val images = this.orEmpty()
        .filter { !it.imageUrl.isNullOrBlank() }
        .map { StoreImageDto(imageUrl = it.imageUrl.orEmpty(), width = it.width, height = it.height) }
    if (images.isNotEmpty()) return images
    return if (fallbackImageUrl.isNullOrBlank()) listOf() else listOf(StoreImageDto(imageUrl = fallbackImageUrl))
}
