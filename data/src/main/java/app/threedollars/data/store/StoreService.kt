package app.threedollars.data.store

import app.threedollars.data.BaseResponse
import app.threedollars.data.store.response.StoresAroundResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

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
}