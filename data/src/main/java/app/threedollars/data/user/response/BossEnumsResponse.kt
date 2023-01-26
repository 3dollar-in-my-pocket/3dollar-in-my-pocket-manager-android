package app.threedollars.data.user.response


import app.threedollars.data.BaseResponse
import app.threedollars.data.user.model.EnumsModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BossEnumsResponse(
    @Json(name = "PaymentMethodType")
    val paymentMethodType: List<EnumsModel> = listOf(),
    @Json(name = "BossStoreOpenType")
    val bossStoreOpenType: List<EnumsModel> = listOf(),
    @Json(name = "FeedbackTargetType")
    val feedbackTargetType: List<EnumsModel> = listOf(),
    @Json(name = "AdvertisementPositionType")
    val advertisementPositionType: List<EnumsModel> = listOf(),
    @Json(name = "UserMenuCategoryType")
    val userMenuCategoryType: List<EnumsModel> = listOf(),
    @Json(name = "PushPlatformType")
    val pushPlatformType: List<EnumsModel> = listOf(),
    @Json(name = "BossRegistrationStatus")
    val bossRegistrationStatus: List<EnumsModel> = listOf(),
    @Json(name = "StoreType")
    val storeType: List<EnumsModel> = listOf(),
    @Json(name = "ApplicationType")
    val applicationType: List<EnumsModel> = listOf(),
    @Json(name = "DayOfTheWeek")
    val dayOfTheWeek: List<EnumsModel> = listOf(),
    @Json(name = "PushOptionsType")
    val pushOptionsType: List<EnumsModel> = listOf(),
    @Json(name = "FileType")
    val fileType: List<EnumsModel> = listOf(),
    @Json(name = "BossAccountSocialType")
    val bossAccountSocialType: List<EnumsModel> = listOf(),
    @Json(name = "BoosRegistrationRejectReasonType")
    val boosRegistrationRejectReasonType: List<EnumsModel> = listOf(),
    @Json(name = "DeleteReasonType")
    val deleteReasonType: List<EnumsModel> = listOf(),
    @Json(name = "VisitType")
    val visitType: List<EnumsModel> = listOf(),
    @Json(name = "FaqCategory")
    val faqCategory: List<EnumsModel> = listOf(),
    @Json(name = "StoreSalesType")
    val storeSalesType: List<EnumsModel> = listOf(),
    @Json(name = "UserSocialType")
    val userSocialType: List<EnumsModel> = listOf(),
    @Json(name = "FeedbackEmojiType")
    val feedbackEmojiType: List<EnumsModel> = listOf()
) : BaseResponse<BossEnumsResponse>()
