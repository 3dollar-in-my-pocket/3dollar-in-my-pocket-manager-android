package app.threedollars.manager.util

import app.threedollars.common.ext.toBooleanDefault
import app.threedollars.common.ext.toDoubleDefault
import app.threedollars.common.ext.toIntDefault
import app.threedollars.common.ext.toStringDefault
import app.threedollars.domain.dto.AppearanceDaysDto
import app.threedollars.domain.dto.BossAccountInfoDto
import app.threedollars.domain.dto.BossEnumsDto
import app.threedollars.domain.dto.BossStoreRetrieveAroundDto
import app.threedollars.domain.dto.BossStoreRetrieveDto
import app.threedollars.domain.dto.CategoriesDto
import app.threedollars.domain.dto.CategoryInfoDto
import app.threedollars.domain.dto.ContentsDto
import app.threedollars.domain.dto.CursorDto
import app.threedollars.domain.dto.EnumsDto
import app.threedollars.domain.dto.FaqCategoriesDto
import app.threedollars.domain.dto.FaqDto
import app.threedollars.domain.dto.FavoriteDto
import app.threedollars.domain.dto.FeedbackFullDto
import app.threedollars.domain.dto.FeedbackSpecificDto
import app.threedollars.domain.dto.FeedbackTypesDto
import app.threedollars.domain.dto.FeedbacksDto
import app.threedollars.domain.dto.ImageUploadDto
import app.threedollars.domain.dto.LocationDto
import app.threedollars.domain.dto.LoginDto
import app.threedollars.domain.dto.MenusDto
import app.threedollars.domain.dto.OpenStatusDto
import app.threedollars.domain.dto.OpeningHoursDto
import app.threedollars.domain.dto.StoreCategoriesDto
import app.threedollars.manager.vo.AppearanceDaysVo
import app.threedollars.manager.vo.BossAccountInfoVo
import app.threedollars.manager.vo.BossEnumsVo
import app.threedollars.manager.vo.BossStoreRetrieveAroundVo
import app.threedollars.manager.vo.BossStoreRetrieveVo
import app.threedollars.manager.vo.CategoriesVo
import app.threedollars.manager.vo.CategoryInfoVo
import app.threedollars.manager.vo.ContentsVo
import app.threedollars.manager.vo.CursorVo
import app.threedollars.manager.vo.EnumsVo
import app.threedollars.manager.vo.FaqCategoriesVo
import app.threedollars.manager.vo.FaqVo
import app.threedollars.manager.vo.FavoriteVo
import app.threedollars.manager.vo.FeedbackFullVo
import app.threedollars.manager.vo.FeedbackSpecificVo
import app.threedollars.manager.vo.FeedbackTypesVo
import app.threedollars.manager.vo.FeedbacksVo
import app.threedollars.manager.vo.ImageUploadVo
import app.threedollars.manager.vo.LocationVo
import app.threedollars.manager.vo.LoginVo
import app.threedollars.manager.vo.MenusVo
import app.threedollars.manager.vo.OpenStatusVo
import app.threedollars.manager.vo.OpeningHoursVo
import app.threedollars.manager.vo.StoreCategoriesVo

fun AppearanceDaysDto.dtoToVo() = AppearanceDaysVo(
    dayOfTheWeek = dayOfTheWeek.toStringDefault(),
    openingHours = openingHours.dtoToVo(),
    locationDescription = locationDescription.toStringDefault(),
)

fun BossAccountInfoDto.dtoToVo() = BossAccountInfoVo(
    bossId = bossId,
    businessNumber = businessNumber,
    createdAt = createdAt,
    isSetupNotification = isSetupNotification,
    name = name,
    socialType = socialType,
    updatedAt = updatedAt,
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
    feedbackEmojiType = feedbackEmojiType.map { it.dtoToVo() },
    bankType = bankType.map { it.dtoToVo() },
)

fun BossStoreRetrieveAroundDto.dtoToVo() = BossStoreRetrieveAroundVo(
    bossStoreId = bossStoreId.toStringDefault(),
    categories = categories.map { it.dtoToVo() },
    createdAt = createdAt.toStringDefault(),
    distance = distance.toIntDefault(),
    location = location?.dtoToVo() ?: LocationVo(),
    menus = menus.map { it.dtoToVo() },
    name = name.toStringDefault(),
    openStatus = openStatus.dtoToVo(),
    totalFeedbacksCounts = totalFeedbacksCounts.toIntDefault(),
    updatedAt = updatedAt.toStringDefault(),
)

fun BossStoreRetrieveDto?.dtoToVo() = BossStoreRetrieveVo(
    appearanceDays = this?.appearanceDays.orEmpty().map { it.dtoToVo() },
    bossStoreId = this?.bossStoreId.toStringDefault(),
    categories = this?.categories.orEmpty().map { it.dtoToVo() },
    createdAt = this?.createdAt.toStringDefault(),
    distance = this?.distance.toIntDefault(),
    favorite = this?.favorite.dtoToVo(),
    imageUrl = this?.imageUrl.toStringDefault(),
    introduction = this?.introduction.toStringDefault(),
    location = this?.location.dtoToVo(),
    menus = this?.menus.orEmpty().map { it.dtoToVo() },
    name = this?.name.toStringDefault(),
    openStatus = this?.openStatus.dtoToVo(),
    snsUrl = this?.snsUrl.toStringDefault(),
    updatedAt = this?.updatedAt.toStringDefault(),
)

fun CategoriesDto.dtoToVo() = CategoriesVo(
    category = category.toStringDefault(),
    categoryId = categoryId.toStringDefault(),
    description = description.toStringDefault(),
    imageUrl = imageUrl.toStringDefault(),
    isNew = isNew ?: false,
    name = name.toStringDefault(),
)

fun CategoryInfoDto.dtoToVo() = CategoryInfoVo(
    category = category,
    description = description,
    displayOrder = displayOrder,
)

fun ContentsDto.dtoToVo() = ContentsVo(
    date = date,
    feedbacks = feedbacks.map { it.dtoToVo() },
)

fun CursorDto.dtoToVo() = CursorVo(
    hasMore = hasMore,
    nextCursor = nextCursor,
)

fun EnumsDto.dtoToVo() = EnumsVo(
    key = key,
    description = description,
)

fun FaqCategoriesDto.dtoToVo() = FaqCategoriesVo(
    category = category,
    description = description,
    displayOrder = displayOrder,
)

fun FaqDto.dtoToVo() = FaqVo(
    answer = answer,
    categoryInfo = categoryInfo.dtoToVo(),
    question = question,
)

fun FavoriteDto?.dtoToVo() = FavoriteVo(
    isFavorite = this?.isFavorite?.toBooleanDefault() ?: false,
)

fun FeedbackFullDto.dtoToVo() = FeedbackFullVo(
    count = count,
    feedbackType = feedbackType,
    ratio = ratio,
)

fun FeedbackSpecificDto.dtoToVo() = FeedbackSpecificVo(
    contents = contents.map { it.dtoToVo() },
    cursor = cursor.dtoToVo(),
)

fun FeedbacksDto.dtoToVo() = FeedbacksVo(
    count = count,
    feedbackType = feedbackType,
)

fun FeedbackTypesDto.dtoToVo() = FeedbackTypesVo(
    description = description,
    emoji = emoji,
    feedbackType = feedbackType,
)

fun ImageUploadDto.dtoToVo() = ImageUploadVo(
    imageUrl = imageUrl,
)

fun LocationDto?.dtoToVo() = LocationVo(
    latitude = this?.latitude.toDoubleDefault(),
    longitude = this?.longitude.toDoubleDefault(),
)

fun LoginDto.dtoToVo() = LoginVo(
    bossId = bossId,
    token = token,
)

fun MenusDto.dtoToVo() = MenusVo(
    imageUrl = imageUrl,
    name = name,
    price = price,
)

fun OpeningHoursDto?.dtoToVo() = OpeningHoursVo(
    startTime = this?.startTime.toStringDefault(),
    endTime = this?.endTime.toStringDefault(),
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
    name = name,
)
