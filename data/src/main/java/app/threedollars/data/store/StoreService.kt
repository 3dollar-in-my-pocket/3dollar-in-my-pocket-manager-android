package app.threedollars.data.store

import app.threedollars.data.BaseResponse
import app.threedollars.data.store.response.MyAccountResponse
import app.threedollars.data.store.response.MyStoreResponse
import app.threedollars.data.store.response.StoresAroundResponse
import retrofit2.Response
import retrofit2.http.*

interface StoreService {

    @GET("v1/boss/stores/around")
    suspend fun getStoresAround(
        @Header("Authorization") authorization: String,
        @Query("categoryId") categoryId: String?,
        @Query("distanceKm") distanceKm: Double?,
        @Query("mapLatitude") mapLatitude: String,
        @Query("mapLongitude") mapLongitude: String,
        @Query("orderType") orderType: String?,
        @Query("size") size: Int?
    ): Response<BaseResponse<List<StoresAroundResponse>>>

    @GET("v1/boss/store/me")
    suspend fun getMyStore(
        @Header("Authorization") authorization: String
    ): Response<BaseResponse<MyStoreResponse>>

    @GET("v1/boss/account/me")
    suspend fun getMyAccount(
        @Header("Authorization") authorization: String
    ): Response<BaseResponse<MyAccountResponse>>

    @DELETE("v1/boss/store/{bossStoreId}/close")
    suspend fun closeStore(
        @Header("Authorization") authorization: String,
        @Path("bossStoreId") bossStoreId: String
    ): Response<BaseResponse<String>>

    @POST("v1/boss/store/{bossStoreId}/open")
    suspend fun openStore(
        @Header("Authorization") authorization: String,
        @Path("bossStoreId") bossStoreId: String,
        @Query("mapLatitude") mapLatitude: String,
        @Query("mapLongitude") mapLongitude: String
    ): Response<BaseResponse<String>>

    @PUT("v1/boss/store/{bossStoreId}/renew")
    suspend fun renewStore(
        @Header("Authorization") authorization: String,
        @Path("bossStoreId") bossStoreId: String,
        @Query("mapLatitude") mapLatitude: String,
        @Query("mapLongitude") mapLongitude: String
    ): Response<BaseResponse<String>>
}