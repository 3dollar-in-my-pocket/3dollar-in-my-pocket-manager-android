package app.threedollars.data.store

import app.threedollars.data.BaseResponse
import app.threedollars.data.store.response.StoresAroundResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StoreService {

    @GET("/v1/boss/stores/around")
    fun getStoresAround(
        @Header("Authorization") authorization: String,
        @Query("categoryId") categoryId: String? = null,
        @Query("distanceKm") distanceKm: Double? = 1.0,
        @Query("mapLatitude") mapLatitude: String,
        @Query("mapLongitude") mapLongitude: String,
        @Query("orderType") orderType: String? = null,
        @Query("size") size: Int? = null
    ): Response<BaseResponse<StoresAroundResponse>>
}