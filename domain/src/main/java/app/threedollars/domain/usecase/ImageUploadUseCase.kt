package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.ImageUploadDto
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface ImageUploadUseCase {
    fun postImageUpload(fileType: String, requestBody: RequestBody): Flow<Resource<ImageUploadDto>>

    fun postImageUploadBulk(fileType: String, requestBodyList: List<RequestBody>): Flow<Resource<List<ImageUploadDto>>>
}