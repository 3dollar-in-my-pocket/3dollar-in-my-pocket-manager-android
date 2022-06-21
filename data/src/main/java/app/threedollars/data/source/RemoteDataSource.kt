package app.threedollars.data.source

import app.threedollars.data.BaseResponse
import app.threedollars.data.store.request.StoresAroundRequest
import app.threedollars.data.store.response.MyAccountResponse
import app.threedollars.data.store.response.MyStoreResponse
import app.threedollars.data.store.response.StoresAroundResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteDataSource {
    fun getStoresAround(
        authorization: String,
        storesAroundRequest: StoresAroundRequest
    ): Flow<Response<BaseResponse<List<StoresAroundResponse>>>>

    fun getMyStore(
        authorization: String
    ): Flow<Response<BaseResponse<MyStoreResponse>>>

    fun getMyAccount(
        authorization: String
    ): Flow<Response<BaseResponse<MyAccountResponse>>>
}