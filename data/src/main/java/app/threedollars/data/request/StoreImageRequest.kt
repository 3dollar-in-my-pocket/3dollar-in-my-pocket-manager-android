package app.threedollars.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreImageRequest(
    @SerialName("imageUrl")
    val imageUrl: String,
)

internal fun BossStoreRequest.withRepresentativeImages(imageUrls: List<String>?): BossStoreRequest =
    if (imageUrls == null) this else copy(representativeImages = imageUrls.map { StoreImageRequest(imageUrl = it) })
