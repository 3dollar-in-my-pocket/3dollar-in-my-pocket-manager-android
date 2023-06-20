package app.threedollars.source

import android.content.Context
import app.threedollars.common.Resource
import app.threedollars.common.ext.toast
import app.threedollars.data.BaseResponse
import app.threedollars.data.request.*
import app.threedollars.data.response.*
import app.threedollars.network.NetworkService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(@ApplicationContext private val context: Context, private val networkService: NetworkService) :
    RemoteDataSource {
    override fun login(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> = flow {
        emit(safeApiCall(networkService.login(loginRequest), context))
    }

    override fun logout(): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.logout(), context))
    }

    override fun signUp(signUpRequest: SignUpRequest): Flow<Resource<LoginResponse>> = flow {
        emit(safeApiCall(networkService.signUp(signUpRequest), context))
    }

    override fun signOut(): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.signOut(), context))
    }

    override fun getBossAccount(): Flow<Resource<BossAccountInfoResponse>> = flow {
        emit(safeApiCall(networkService.getBossAccount(), context))
    }

    override fun putBossAccount(bossAccountInfoRequest: BossAccountInfoRequest): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.putBossAccount(bossAccountInfoRequest), context))
    }

    override fun putBossDevice(bossDeviceRequest: BossDeviceRequest): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.putBossDevice(bossDeviceRequest), context))
    }

    override fun deleteBossDevice(): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.deleteBossDevice(), context))
    }

    override fun putBossDeviceToken(bossDeviceRequest: BossDeviceRequest): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.putBossDeviceToken(bossDeviceRequest), context))
    }

    override fun putBossStore(bossStoreId: String, bossStoreRequest: BossStoreRequest): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.putBossStore(bossStoreId, bossStoreRequest), context))
    }

    override fun patchBossStore(bossStoreId: String, bossStoreRequest: BossStoreRequest): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.patchBossStore(bossStoreId, bossStoreRequest), context))
    }

    override fun deleteBossStoreOpen(bossStoreId: String): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.deleteBossStoreOpen(bossStoreId), context))
    }

    override fun postBossStoreOpen(bossStoreId: String, mapLatitude: Double, mapLongitude: Double): Flow<Resource<String>> = flow {
        emit(safeApiCall(networkService.postBossStoreOpen(bossStoreId, mapLatitude, mapLongitude), context))
    }

    override fun getBossStoreRetrieveSpecific(
        bossStoreId: String,
        latitude: Double,
        longitude: Double
    ): Flow<Resource<BossStoreRetrieveResponse>> = flow {
        emit(safeApiCall(networkService.getBossStoreRetrieveSpecific(bossStoreId, latitude, longitude), context))
    }

    override fun getBossStoreRetrieveMe(): Flow<Resource<BossStoreRetrieveResponse>> = flow {
        emit(safeApiCall(networkService.getBossStoreRetrieveMe(), context))
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
    ): Flow<Resource<List<BossStoreRetrieveAroundResponse>>> = flow {
        emit(
            safeApiCall(
                networkService.getBossStoreRetrieveAround(
                    categoryId,
                    distanceKm,
                    latitude,
                    longitude,
                    mapLatitude,
                    mapLongitude,
                    orderType,
                    size
                ), context
            )
        )
    }

    override fun getBossEnums(): Flow<Resource<BossEnumsResponse>> = flow {
        emit(safeApiCall(networkService.getBossEnums(), context))
    }

    override fun getFaqCategories(): Flow<Resource<List<FaqCategoriesResponse>>> = flow {
        emit(safeApiCall(networkService.getFaqCategories(), context))
    }

    override fun getFaqs(category: String): Flow<Resource<List<FaqResponse>>> = flow {
        emit(safeApiCall(networkService.getFaqs(category), context))
    }

    override fun getFeedbackFull(targetType: String, targetId: String): Flow<Resource<List<FeedbackFullResponse>>> = flow {
        emit(safeApiCall(networkService.getFeedbackFull(targetType, targetId), context))
    }

    override fun getFeedbackTypes(targetType: String): Flow<Resource<List<FeedbackTypesResponse>>> = flow {
        emit(safeApiCall(networkService.getFeedbackTypes(targetType), context))
    }

    override fun postImageUpload(fileType: String, requestBody: RequestBody): Flow<Resource<ImageUploadResponse>> {
        return flow {
            val multipartBody = MultipartBody.Part.createFormData(
                name = "file",
                filename = "file.jpeg",
                body = requestBody
            )
            emit(safeApiCall(networkService.postImageUpload(fileType, multipartBody), context))
        }
    }

    override fun postImageUploadBulk(fileType: String, requestBodyList: List<RequestBody>): Flow<Resource<List<ImageUploadResponse>>> =
        flow {
            val multipartBodyList = requestBodyList.mapIndexed { index, requestBody ->
                MultipartBody.Part.createFormData(
                    name = "files",
                    filename = "file$index.jpeg",
                    body = requestBody
                )
            }
            emit(safeApiCall(networkService.postImageUploadBulk(fileType, multipartBodyList), context))
        }

    override fun getStoreCategories(storeType: String): Flow<Resource<List<StoreCategoriesResponse>>> = flow {
        emit(safeApiCall(networkService.getStoreCategories(storeType), context))
    }

}

fun <T> safeApiCall(response: Response<BaseResponse<T>>, context: Context): Resource<T> {
    return try {
        if (response.isSuccessful) {
            if (response.code().toString() != "200") {
                context.toast(response.message())
            }
            Resource.Success(data = response.body()?.data!!, code = response.code().toString())
        } else {
            Resource.Error(
                errorMessage = response.errorBody()?.string(),
                code = response.code().toString()
            )
        }

    } catch (e: HttpException) {
        Resource.Error(errorMessage = e.message ?: "Something went wrong", code = null)
    } catch (e: IOException) {
        Resource.Error(errorMessage = e.message ?: "Please check your network connection", code = null)
    } catch (e: Exception) {
        Resource.Error(errorMessage = e.message ?: "Something went wrong", code = null)
    }
}
