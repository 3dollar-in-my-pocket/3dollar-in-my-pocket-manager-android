package app.threedollars.data.response


import app.threedollars.data.BaseResponse
import app.threedollars.domain.dto.ImageUploadDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageUploadResponse(
    @Json(name = "imageUrl")
    val imageUrl: String? = null
) : BaseResponse<ImageUploadResponse>() {
    fun toDto() = ImageUploadDto(imageUrl)
}

fun List<ImageUploadResponse>.toDto() = map {
    ImageUploadDto(it.imageUrl)
}

