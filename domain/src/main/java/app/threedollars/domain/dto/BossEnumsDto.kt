package app.threedollars.domain.dto


data class BossEnumsDto(
    val paymentMethodType: List<EnumsDto> = listOf(),
    val bossStoreOpenType: List<EnumsDto> = listOf(),
    val feedbackTargetType: List<EnumsDto> = listOf(),
    val advertisementPositionType: List<EnumsDto> = listOf(),
    val userMenuCategoryType: List<EnumsDto> = listOf(),
    val pushPlatformType: List<EnumsDto> = listOf(),
    val bossRegistrationStatus: List<EnumsDto> = listOf(),
    val storeType: List<EnumsDto> = listOf(),
    val applicationType: List<EnumsDto> = listOf(),
    val dayOfTheWeek: List<EnumsDto> = listOf(),
    val pushOptionsType: List<EnumsDto> = listOf(),
    val fileType: List<EnumsDto> = listOf(),
    val bossAccountSocialType: List<EnumsDto> = listOf(),
    val boosRegistrationRejectReasonType: List<EnumsDto> = listOf(),
    val deleteReasonType: List<EnumsDto> = listOf(),
    val visitType: List<EnumsDto> = listOf(),
    val faqCategory: List<EnumsDto> = listOf(),
    val storeSalesType: List<EnumsDto> = listOf(),
    val userSocialType: List<EnumsDto> = listOf(),
    val feedbackEmojiType: List<EnumsDto> = listOf()
)
