package app.threedollars.manager.util

import app.threedollars.common.ext.toBooleanDefault
import app.threedollars.common.ext.toDoubleDefault
import app.threedollars.common.ext.toIntDefault
import app.threedollars.common.ext.toStringDefault
import app.threedollars.domain.dto.*
import app.threedollars.manager.vo.*

fun AppearanceDaysDto.dtoToVo() = AppearanceDaysVo(
    dayOfTheWeek = dayOfTheWeek,
    openingHours = openingHours?.dtoToVo(),
    locationDescription = locationDescription
)

fun BossAccountInfoDto.dtoToVo() = BossAccountInfoVo(
    bossId = bossId,
    businessNumber = businessNumber,
    createdAt = createdAt,
    isSetupNotification = isSetupNotification,
    name = name,
    socialType = socialType,
    updatedAt = updatedAt
)

fun BossEnumsDto.dtoToVo() = BossEnumsVo(
    paymentMethodType = paymentMethodType.map { it.dtoToVo() },
    bossStoreOpenType = bossStoreOpenType.map { it.dtoToVo() },
    feedbackTargetType = feedbackTargetType.map { it.dtoToVo() },
    advertisementPositionType = advertisementPositionType.map { it.dtoToVo() },
    userMenuCategoryType = userMenuCategoryType.map { it.dtoToVo() },
    pushPlatformType = pushPlatformType.map { it.dtoToVo() },
    bossRegistrationStatus = bossRegistrationStatus.map { it.dtoToVo() },
    storeType = storeType.map { it.dtoToVo() },
    applicationType = applicationType.map { it.dtoToVo() },
    dayOfTheWeek = dayOfTheWeek.map { it.dtoToVo() },
    pushOptionsType = pushOptionsType.map { it.dtoToVo() },
    fileType = fileType.map { it.dtoToVo() },
    bossAccountSocialType = bossAccountSocialType.map { it.dtoToVo() },
    boosRegistrationRejectReasonType = boosRegistrationRejectReasonType.map { it.dtoToVo() },
    deleteReasonType = deleteReasonType.map { it.dtoToVo() },
    visitType = visitType.map { it.dtoToVo() },
    faqCategory = faqCategory.map { it.dtoToVo() },
    storeSalesType = storeSalesType.map { it.dtoToVo() },
    userSocialType = userSocialType.map { it.dtoToVo() },
    feedbackEmojiType = feedbackEmojiType.map { it.dtoToVo() }
)

fun BossStoreRetrieveAroundDto.dtoToVo() = BossStoreRetrieveAroundVo(
    bossStoreId = bossStoreId,
    categories = categories.map { it.dtoToVo() },
    createdAt = createdAt,
    distance = distance,
    location = location?.dtoToVo(),
    menus = menus.map { it.dtoToVo() },
    name = name,
    openStatus = openStatus.dtoToVo(),
    totalFeedbacksCounts = totalFeedbacksCounts,
    updatedAt = updatedAt
)

fun BossStoreRetrieveDto.dtoToVo() = BossStoreRetrieveVo(
    appearanceDays = appearanceDays.map { it.dtoToVo() },
    bossStoreId = bossStoreId.toStringDefault(),
    categories = categories.map { it.dtoToVo() },
    createdAt = createdAt.toStringDefault(),
    distance = distance.toIntDefault(),
    favorite = favorite.dtoToVo(),
    imageUrl = imageUrl.toStringDefault(),
    introduction = introduction.toStringDefault(),
    location = location.dtoToVo(),
    menus = menus.map { it.dtoToVo() },
    name = name.toStringDefault(),
    openStatus = openStatus.dtoToVo(),
    snsUrl = snsUrl.toStringDefault(),
    updatedAt = updatedAt.toStringDefault()
)

fun CategoriesDto.dtoToVo() = CategoriesVo(
    category = category,
    categoryId = categoryId,
    description = description,
    imageUrl = imageUrl,
    isNew = isNew,
    name = name
)

fun CategoryInfoDto.dtoToVo() = CategoryInfoVo(
    category = category,
    description = description,
    displayOrder = displayOrder
)

fun ContentsDto.dtoToVo() = ContentsVo(
    date = date,
    feedbacks = feedbacks.map { it.dtoToVo() }
)

fun CursorDto.dtoToVo() = CursorVo(
    hasMore = hasMore,
    nextCursor = nextCursor
)

fun EnumsDto.dtoToVo() = EnumsVo(
    category = category,
    description = description,
    displayOrder = displayOrder
)

fun FaqCategoriesDto.dtoToVo() = FaqCategoriesVo(
    category = category,
    description = description,
    displayOrder = displayOrder
)

fun FaqDto.dtoToVo() = FaqVo(
    answer = answer,
    category = category,
    categoryInfo = categoryInfo?.dtoToVo(),
    createdAt = createdAt,
    faqId = faqId,
    question = question,
    updatedAt = updatedAt
)

fun FavoriteDto?.dtoToVo() = FavoriteVo(
    isFavorite = this?.isFavorite?.toBooleanDefault() ?: false
)

fun FeedbackFullDto.dtoToVo() = FeedbackFullVo(
    count = count,
    feedbackType = feedbackType,
    ratio = ratio
)

fun FeedbackSpecificDto.dtoToVo() = FeedbackSpecificVo(
    contents = contents.map { it.dtoToVo() },
    cursor = cursor.dtoToVo(),
)

fun FeedbacksDto.dtoToVo() = FeedbacksVo(
    count = count,
    feedbackType = feedbackType
)

fun FeedbackTypesDto.dtoToVo() = FeedbackTypesVo(
    description = description,
    emoji = emoji,
    feedbackType = feedbackType
)

fun ImageUploadDto.dtoToVo() = ImageUploadVo(
    imageUrl = imageUrl
)

fun LocationDto?.dtoToVo() = LocationVo(
    latitude = this?.latitude?.toDoubleDefault(),
    longitude = this?.longitude?.toDoubleDefault()
)

fun LoginDto.dtoToVo() = LoginVo(
    bossId = bossId,
    token = token
)

fun MenusDto.dtoToVo() = MenusVo(
    imageUrl = imageUrl,
    name = name,
    price = price
)

fun OpeningHoursDto.dtoToVo() = OpeningHoursVo(
    startTime = startTime,
    endTime = endTime
)

fun OpenStatusDto?.dtoToVo() = OpenStatusVo(
    openStartDateTime = this?.openStartDateTime?.toStringDefault(),
    status = this?.status?.toStringDefault(),
)

fun StoreCategoriesDto.dtoToVo() = StoreCategoriesVo(
    category = category,
    categoryId = categoryId,
    description = description,
    imageUrl = imageUrl,
    isNew = isNew,
    name = name
)