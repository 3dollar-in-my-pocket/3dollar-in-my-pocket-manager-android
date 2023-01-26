package app.threedollars.source

import app.threedollars.data.BaseResponse
import app.threedollars.data.request.*
import app.threedollars.data.response.*
import app.threedollars.network.NetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val networkService: NetworkService) : RemoteDataSource {
    override fun login(loginRequest: LoginRequest): Flow<Response<BaseResponse<LoginResponse>>> = flow {
        emit(networkService.login(loginRequest))
    }

    override fun logout(): Flow<Response<BaseResponse<String>>> = flow {
        emit(networkService.logout())
    }

    override fun signUp(signUpRequest: SignUpRequest): Flow<Response<BaseResponse<String>>> = flow {
        emit(networkService.signUp(signUpRequest))
    }

    override fun signOut(): Flow<Response<BaseResponse<String>>> = flow {
        emit(networkService.signOut())
    }

    override fun getBossAccount(): Flow<Response<BaseResponse<BossAccountInfoResponse>>> = flow {
        emit(networkService.getBossAccount())
    }

    override fun putBossAccount(bossAccountInfoRequest: BossAccountInfoRequest): Flow<Response<BaseResponse<String>>> = flow {
        emit(networkService.putBossAccount(bossAccountInfoRequest))
    }

    override fun putBossDevice(bossDeviceRequest: BossDeviceRequest): Flow<Response<BaseResponse<String>>> = flow {
        emit(networkService.putBossDevice(bossDeviceRequest))
    }

    override fun deleteBossDevice(): Flow<Response<BaseResponse<String>>> = flow {
        emit(networkService.deleteBossDevice())
    }

    override fun putBossDeviceToken(bossDeviceRequest: BossDeviceRequest): Flow<Response<BaseResponse<String>>> = flow {
        emit(networkService.putBossDeviceToken(bossDeviceRequest))
    }

    override fun putBossStore(bossStoreId: String, bossStoreRequest: BossStoreRequest): Flow<Response<BaseResponse<String>>> = flow {
        emit(networkService.putBossStore(bossStoreId, bossStoreRequest))
    }

    override fun patchBossStore(bossStoreId: String, bossStoreRequest: BossStoreRequest): Flow<Response<BaseResponse<String>>> = flow {
        emit(networkService.patchBossStore(bossStoreId, bossStoreRequest))
    }

    override fun deleteBossStoreOpen(bossStoreId: String): Flow<Response<BaseResponse<String>>> = flow {
        emit(networkService.deleteBossStoreOpen(bossStoreId))
    }

    override fun postBossStoreOpen(bossStoreId: String, mapLatitude: Double, mapLongitude: Double): Flow<Response<BaseResponse<String>>> = flow {
        emit(networkService.postBossStoreOpen(bossStoreId, mapLatitude, mapLongitude))
    }

    override fun getBossStoreRetrieveSpecific(
        bossStoreId: String,
        latitude: Double,
        longitude: Double
    ): Flow<Response<BaseResponse<BossStoreRetrieveResponse>>> = flow {
        emit(networkService.getBossStoreRetrieveSpecific(bossStoreId, latitude, longitude))
    }

    override fun getBossStoreRetrieveMe(): Flow<Response<BaseResponse<BossStoreRetrieveResponse>>> = flow {
        emit(networkService.getBossStoreRetrieveMe())
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
    ): Flow<Response<BaseResponse<List<BossStoreRetrieveAroundResponse>>>> = flow {
        emit(networkService.getBossStoreRetrieveAround(categoryId, distanceKm, latitude, longitude, mapLatitude, mapLongitude, orderType, size))
    }

    override fun getBossEnums(): Flow<Response<BaseResponse<BossEnumsResponse>>> = flow {
        emit(networkService.getBossEnums())
    }

    override fun getFaqCategories(): Flow<Response<BaseResponse<List<FaqCategoriesResponse>>>> = flow {
        emit(networkService.getFaqCategories())
    }

    override fun getFaqs(category: String): Flow<Response<BaseResponse<List<FaqResponse>>>> = flow {
        emit(networkService.getFaqs(category))
    }

    override fun getFeedbackFull(targetType: String, targetId: String): Flow<Response<BaseResponse<List<FeedbackFullResponse>>>> = flow {
        emit(networkService.getFeedbackFull(targetType, targetId))
    }

    override fun getFeedbackSpecific(
        targetType: String,
        targetId: String,
        startDAte: String,
        endDate: String
    ): Flow<Response<BaseResponse<FeedbackSpecificResponse>>> = flow {
        emit(networkService.getFeedbackSpecific(targetType, targetId, startDAte, endDate))
    }

    override fun getFeedbackTypes(targetType: String): Flow<Response<BaseResponse<List<FeedbackTypesResponse>>>> = flow {
        emit(networkService.getFeedbackTypes(targetType))
    }

    override fun postImageUpload(fileType: String, file: MultipartBody.Part): Flow<Response<BaseResponse<ImageUploadResponse>>> = flow {
        emit(networkService.postImageUpload(fileType, file))
    }

    override fun postImageUploadBulk(fileType: String, fileList: List<MultipartBody.Part>): Flow<Response<BaseResponse<List<ImageUploadResponse>>>> =
        flow {
            emit(networkService.postImageUploadBulk(fileType, fileList))
        }

    override fun getStoreCategories(storeType: String): Flow<Response<BaseResponse<List<StoreCategoriesResponse>>>> = flow {
        emit(networkService.getStoreCategories(storeType))
    }
}