package app.threedollars.data.user

import app.threedollars.data.user.response.CategoryListResponse
import retrofit2.Response
import retrofit2.http.GET

interface StoreService {

    @GET("/boss/v1/boss/store/categories")
    fun getCategories(): Response<CategoryListResponse>
}