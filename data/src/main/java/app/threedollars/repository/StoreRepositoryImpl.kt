package app.threedollars.repository

import app.threedollars.Resource
import app.threedollars.dto.*
import app.threedollars.dto.AppearanceDaysRequestDto
import app.threedollars.dto.MenusDto
import app.threedollars.source.RemoteDataSource
import okhttp3.MultipartBody
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource) : StoreRepository {
    override fun putBossStore(
        bossStoreId: String,
        appearanceDays: List<AppearanceDaysRequestDto>,
        categoriesIds: List<String>,
        imageUrl: String?,
        introduction: String?,
        menus: List<MenusDto>,
        name: String?,
        snsUrl: String?
    ): Resource<String> {
        TODO("Not yet implemented")
    }

    override fun patchBossStore(
        bossStoreId: String,
        appearanceDays: List<AppearanceDaysRequestDto>,
        categoriesIds: List<String>,
        imageUrl: String?,
        introduction: String?,
        menus: List<MenusDto>,
        name: String?,
        snsUrl: String?
    ): Resource<String> {
        TODO("Not yet implemented")
    }

    override fun deleteBossStoreOpen(bossStoreId: String): Resource<String> {
        TODO("Not yet implemented")
    }

    override fun postBossStoreOpen(bossStoreId: String, mapLatitude: Double, mapLongitude: Double): Resource<String> {
        TODO("Not yet implemented")
    }

    override fun getBossStoreRetrieveSpecific(bossStoreId: String, latitude: Double, longitude: Double): Resource<BossStoreRetrieveDto> {
        TODO("Not yet implemented")
    }

    override fun getBossStoreRetrieveMe(): Resource<BossStoreRetrieveDto> {
        TODO("Not yet implemented")
    }

    override fun getBossStoreRetrieveAround(
        categoryId: String,
        distanceKm: Int,
        latitude: Double,
        longitude: Double,
        mapLatitude: Double,
        mapLongitude: Double,
        orderType: String,
        size: Int
    ): Resource<List<BossStoreRetrieveAroundDto>> {
        TODO("Not yet implemented")
    }

    override fun getBossEnums(): Resource<BossEnumsDto> {
        TODO("Not yet implemented")
    }

    override fun getFaqCategories(): Resource<List<FaqCategoriesDto>> {
        TODO("Not yet implemented")
    }

    override fun getFaqs(category: String): Resource<List<FaqDto>> {
        TODO("Not yet implemented")
    }

    override fun getFeedbackFull(targetType: String, targetId: String): Resource<List<FeedbackFullDto>> {
        TODO("Not yet implemented")
    }

    override fun getFeedbackSpecific(targetType: String, targetId: String, startDAte: String, endDate: String): Resource<FeedbackSpecificDto> {
        TODO("Not yet implemented")
    }

    override fun getFeedbackTypes(targetType: String): Resource<List<FeedbackTypesDto>> {
        TODO("Not yet implemented")
    }

    override fun postImageUpload(fileType: String, file: MultipartBody.Part): Resource<ImageUploadDto> {
        TODO("Not yet implemented")
    }

    override fun postImageUploadBulk(fileType: String, fileList: List<MultipartBody.Part>): Resource<List<ImageUploadDto>> {
        TODO("Not yet implemented")
    }

    override fun getStoreCategories(storeType: String): Resource<List<StoreCategoriesDto>> {
        TODO("Not yet implemented")
    }
}