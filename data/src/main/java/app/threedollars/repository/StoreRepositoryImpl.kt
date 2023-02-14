package app.threedollars.repository

import app.threedollars.common.Resource
import app.threedollars.data.model.AppearanceDaysRequestModel
import app.threedollars.data.model.MenusModel
import app.threedollars.data.request.BossStoreRequest
import app.threedollars.data.response.*
import app.threedollars.domain.dto.*
import app.threedollars.domain.repository.StoreRepository
import app.threedollars.source.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    ): Flow<Resource<String>> {
        val appearanceDaysModel = appearanceDays.map {
            AppearanceDaysRequestModel(it.dayOfTheWeek, it.startTime, it.endTime, it.locationDescription)
        }
        val menusModel = menus.map {
            MenusModel(it.imageUrl, it.name, it.price)
        }
        val bossStoreRequest = BossStoreRequest(
            appearanceDaysModel,
            categoriesIds,
            imageUrl,
            introduction,
            menusModel,
            name,
            snsUrl
        )
        return remoteDataSource.putBossStore(bossStoreId, bossStoreRequest)
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
    ): Flow<Resource<String>> {
        val appearanceDaysModel = appearanceDays.map {
            AppearanceDaysRequestModel(it.dayOfTheWeek, it.startTime, it.endTime, it.locationDescription)
        }
        val menusModel = menus.map {
            MenusModel(it.imageUrl, it.name, it.price)
        }
        val bossStoreRequest = BossStoreRequest(
            appearanceDaysModel,
            categoriesIds,
            imageUrl,
            introduction,
            menusModel,
            name,
            snsUrl
        )
        return remoteDataSource.patchBossStore(bossStoreId, bossStoreRequest)
    }

    override fun deleteBossStoreOpen(bossStoreId: String): Flow<Resource<String>> =
        remoteDataSource.deleteBossStoreOpen(bossStoreId)

    override fun postBossStoreOpen(bossStoreId: String, mapLatitude: Double, mapLongitude: Double): Flow<Resource<String>> =
        remoteDataSource.postBossStoreOpen(bossStoreId, mapLatitude, mapLongitude)

    override fun getBossStoreRetrieveSpecific(
        bossStoreId: String,
        latitude: Double,
        longitude: Double
    ): Flow<Resource<BossStoreRetrieveDto>> {
        return remoteDataSource.getBossStoreRetrieveSpecific(bossStoreId, latitude, longitude).map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto())
            } else {
                Resource.Error(errorMessage = it.errorMessage,code = it.code)
            }
        }
    }

    override fun getBossStoreRetrieveMe(): Flow<Resource<BossStoreRetrieveDto>> =
        remoteDataSource.getBossStoreRetrieveMe().map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto())
            } else {
                Resource.Error(errorMessage = it.errorMessage,code = it.code)
            }
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
    ): Flow<Resource<List<BossStoreRetrieveAroundDto>>> {
        return remoteDataSource.getBossStoreRetrieveAround(categoryId, distanceKm, latitude, longitude, mapLatitude, mapLongitude, orderType, size)
            .map {
                if (it.data != null) {
                    Resource.Success(data = it.data!!.toDto())
                } else {
                    Resource.Error(errorMessage = it.errorMessage,code = it.code)
                }
            }
    }

    override fun getBossEnums(): Flow<Resource<BossEnumsDto>> =
        remoteDataSource.getBossEnums().map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto())
            } else {
                Resource.Error(errorMessage = it.errorMessage,code = it.code)
            }
        }

    override fun getFaqCategories(): Flow<Resource<List<FaqCategoriesDto>>> =
        remoteDataSource.getFaqCategories().map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto())
            } else {
                Resource.Error(errorMessage = it.errorMessage,code = it.code)
            }
        }

    override fun getFaqs(category: String): Flow<Resource<List<FaqDto>>> =
        remoteDataSource.getFaqs(category).map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto())
            } else {
                Resource.Error(errorMessage = it.errorMessage,code = it.code)
            }
        }

    override fun getFeedbackFull(targetType: String, targetId: String): Flow<Resource<List<FeedbackFullDto>>> =
        remoteDataSource.getFeedbackFull(targetType, targetId).map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto())
            } else {
                Resource.Error(errorMessage = it.errorMessage,code = it.code)
            }
        }

    override fun getFeedbackSpecific(
        targetType: String,
        targetId: String,
        startDAte: String,
        endDate: String
    ): Flow<Resource<FeedbackSpecificDto>> =
        remoteDataSource.getFeedbackSpecific(targetType, targetId, startDAte, endDate).map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto())
            } else {
                Resource.Error(errorMessage = it.errorMessage,code = it.code)
            }
        }

    override fun getFeedbackTypes(targetType: String): Flow<Resource<List<FeedbackTypesDto>>> =
        remoteDataSource.getFeedbackTypes(targetType).map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto())
            } else {
                Resource.Error(errorMessage = it.errorMessage,code = it.code)
            }
        }

    override fun postImageUpload(fileType: String, file: MultipartBody.Part): Flow<Resource<ImageUploadDto>> =
        remoteDataSource.postImageUpload(fileType, file).map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto())
            } else {
                Resource.Error(errorMessage = it.errorMessage,code = it.code)
            }
        }

    override fun postImageUploadBulk(fileType: String, fileList: List<MultipartBody.Part>): Flow<Resource<List<ImageUploadDto>>> =
        remoteDataSource.postImageUploadBulk(fileType, fileList).map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto())
            } else {
                Resource.Error(errorMessage = it.errorMessage,code = it.code)
            }
        }

    override fun getStoreCategories(storeType: String): Flow<Resource<List<StoreCategoriesDto>>> =
        remoteDataSource.getStoreCategories(storeType).map {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto())
            } else {
                Resource.Error(errorMessage = it.errorMessage,code = it.code)
            }
        }
}