package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.ImageUploadDto
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ImageUploadUseCase {
    fun postImageUpload(fileType: String, file: MultipartBody.Part): Flow<Resource<ImageUploadDto>>

    fun postImageUploadBulk(fileType: String, fileList: List<MultipartBody.Part>): Flow<Resource<List<ImageUploadDto>>>
}