package app.threedollars.repository

import app.threedollars.Resource
import app.threedollars.dto.*
import app.threedollars.dto.AppearanceDaysRequestDto
import app.threedollars.dto.MenusDto
import okhttp3.MultipartBody

interface StoreRepository {

    // boss-store-controller
    fun putBossStore(
        bossStoreId: String,
        appearanceDays: List<AppearanceDaysRequestDto> = listOf(),
        categoriesIds: List<String> = listOf(),
        imageUrl: String? = null,
        introduction: String? = null,
        menus: List<MenusDto> = listOf(),
        name: String? = null,
        snsUrl: String? = null
    ): Resource<String>

    fun patchBossStore(
        bossStoreId: String,
        appearanceDays: List<AppearanceDaysRequestDto> = listOf(),
        categoriesIds: List<String> = listOf(),
        imageUrl: String? = null,
        introduction: String? = null,
        menus: List<MenusDto> = listOf(),
        name: String? = null,
        snsUrl: String? = null
    ): Resource<String>

    // boss-store-open-controller
    fun deleteBossStoreOpen(bossStoreId: String): Resource<String>

    fun postBossStoreOpen(bossStoreId: String, mapLatitude: Double, mapLongitude: Double): Resource<String>

    // boss-store-retrieve-controller
    fun getBossStoreRetrieveSpecific(bossStoreId: String, latitude: Double, longitude: Double): Resource<BossStoreRetrieveDto>

    fun getBossStoreRetrieveMe(): Resource<BossStoreRetrieveDto>

    fun getBossStoreRetrieveAround(
        categoryId: String,
        distanceKm: Int,
        latitude: Double,
        longitude: Double,
        mapLatitude: Double,
        mapLongitude: Double,
        orderType: String,
        size: Int
    ): Resource<List<BossStoreRetrieveAroundDto>>

    // enum-mapper-controller
    fun getBossEnums(): Resource<BossEnumsDto>

    // faq-controller
    fun getFaqCategories(): Resource<List<FaqCategoriesDto>>

    fun getFaqs(category: String = ""): Resource<List<FaqDto>>

    // feedback-controller
    fun getFeedbackFull(targetType: String, targetId: String): Resource<List<FeedbackFullDto>>

    fun getFeedbackSpecific(
        targetType: String,
        targetId: String,
        startDAte: String,
        endDate: String
    ): Resource<FeedbackSpecificDto>

    fun getFeedbackTypes(targetType: String): Resource<List<FeedbackTypesDto>>

    // image-upload-controller
    fun postImageUpload(fileType: String, file: MultipartBody.Part): Resource<ImageUploadDto>

    fun postImageUploadBulk(fileType: String, fileList: List<MultipartBody.Part>): Resource<List<ImageUploadDto>>

    // platform-store-category-controller
    fun getStoreCategories(storeType: String): Resource<List<StoreCategoriesDto>>
}