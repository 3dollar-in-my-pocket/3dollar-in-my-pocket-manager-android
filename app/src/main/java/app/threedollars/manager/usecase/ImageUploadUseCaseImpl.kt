package app.threedollars.manager.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.ImageUploadDto
import app.threedollars.domain.repository.StoreRepository
import app.threedollars.domain.usecase.ImageUploadUseCase
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import javax.inject.Inject


class ImageUploadUseCaseImpl @Inject constructor(private val storeRepository: StoreRepository) : ImageUploadUseCase {
    override fun postImageUpload(fileType: String, requestBody: RequestBody): Flow<Resource<ImageUploadDto>> {
        return storeRepository.postImageUpload(fileType, requestBody)
    }

    override fun postImageUploadBulk(fileType: String, requestBodyList: List<RequestBody>): Flow<Resource<List<ImageUploadDto>>> =
        storeRepository.postImageUploadBulk(fileType, requestBodyList)
}