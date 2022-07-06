package app.threedollars.data.source

import app.threedollars.data.BaseResponse
import app.threedollars.data.store.StoreService
import app.threedollars.data.store.request.StoresAroundRequest
import app.threedollars.data.store.response.MyAccountResponse
import app.threedollars.data.store.response.MyStoreResponse
import app.threedollars.data.store.response.StoresAroundResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val storeService: StoreService) :
    RemoteDataSource {
    override fun getStoresAround(
        authorization: String,
        storesAroundRequest: StoresAroundRequest
    ): Flow<Response<BaseResponse<List<StoresAroundResponse>>>> = flow {
        emit(
            storeService.getStoresAround(
                authorization = authorization,
                categoryId = storesAroundRequest.categoryId,
                distanceKm = storesAroundRequest.distanceKm,
                mapLatitude = storesAroundRequest.mapLatitude,
                mapLongitude = storesAroundRequest.mapLongitude,
                orderType = storesAroundRequest.orderType,
                size = storesAroundRequest.size
            )
        )
    }

    override fun getMyStore(authorization: String): Flow<Response<BaseResponse<MyStoreResponse>>> =
        flow { emit(storeService.getMyStore(authorization = authorization)) }

    override fun getMyAccount(
        authorization: String
    ): Flow<Response<BaseResponse<MyAccountResponse>>> =
        flow { emit(storeService.getMyAccount(authorization)) }

    override fun closeStore(
        authorization: String,
        bossStoreId: String
    ): Flow<Response<BaseResponse<String>>> =
        flow { emit(storeService.closeStore(authorization, bossStoreId)) }

    override fun openStore(
        authorization: String,
        bossStoreId: String,
        mapLatitude: String,
        mapLongitude: String
    ): Flow<Response<BaseResponse<String>>> =
        flow { emit(storeService.openStore(authorization, bossStoreId, mapLatitude, mapLongitude)) }

    override fun renewStore(
        authorization: String,
        bossStoreId: String,
        mapLatitude: String,
        mapLongitude: String
    ): Flow<Response<BaseResponse<String>>> =
        flow { emit(storeService.renewStore(authorization, bossStoreId, mapLatitude, mapLongitude)) }
}