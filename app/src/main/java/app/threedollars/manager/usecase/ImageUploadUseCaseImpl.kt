package app.threedollars.manager.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.ImageUploadDto
import app.threedollars.domain.repository.StoreRepository
import app.threedollars.domain.usecase.ImageUploadUseCase
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class ImageUploadUseCaseImpl @Inject constructor(private val storeRepository: StoreRepository) : ImageUploadUseCase {
    override fun postImageUpload(fileType: String, file: MultipartBody.Part): Flow<Resource<ImageUploadDto>> =
        storeRepository.postImageUpload(fileType, file)

    override fun postImageUploadBulk(fileType: String, fileList: List<MultipartBody.Part>): Flow<Resource<List<ImageUploadDto>>> =
        storeRepository.postImageUploadBulk(fileType, fileList)
}