package app.threedollars.manager.feature.review.model

import app.threedollars.common.ext.toDoubleDefault
import app.threedollars.common.ext.toIntDefault
import app.threedollars.common.ext.toStringDefault
import app.threedollars.domain.dto.BossStoreRetrieveDto
import app.threedollars.domain.dto.CommentPresetDto
import app.threedollars.domain.dto.StoreReviewDto

internal fun BossStoreRetrieveDto?.dtoToVo() = BossStoreRetrieveVo(
    bossStoreId = this?.bossStoreId.toStringDefault(),
    rating = this?.rating.toDoubleDefault(),
    reviewTotalCount = this?.reviewTotalCount.toIntDefault(),
    storeName = this?.name.toStringDefault()
)

internal fun StoreReviewDto.StoreReview.dtoToVo(storeName: String) = ReviewVo(
    reviewId = reviewId,
    rating = rating,
    contents = contents.toStringDefault(),
    images = images.map { it.dtoToVo() },
    writer = writer.dtoToVo(),
    createdAt = createdAt.toStringDefault(),
    sticker = stickers.first().dtoToVo(),
    comment = comments.firstOrNull { comment -> comment.status == "ACTIVE" }?.dtoToVo(),
    storeName = storeName,
    status = status
)

internal fun StoreReviewDto.StoreReview.Image.dtoToVo() = ReviewVo.Image(
    imageUrl = imageUrl,
    width = width,
    height = height
)

internal fun StoreReviewDto.StoreReview.Writer.dtoToVo() = ReviewVo.Writer(
    name = name,
    medal = medal.dtoToVo()
)

internal fun StoreReviewDto.StoreReview.Writer.Medal.dtoToVo() = ReviewVo.Writer.Medal(
    name = name,
    iconUrl = iconUrl
)

internal fun StoreReviewDto.StoreReview.Sticker.dtoToVo() = ReviewVo.Sticker(
    stickerId = stickerId,
    emoji = emoji,
    count = count,
    reactedByMe = reactedByMe
)

internal fun StoreReviewDto.StoreReview.Comment.dtoToVo() = ReviewVo.Comment(
    commentId = commentId,
    content = content,
    status = status,
    isOwner = isOwner,
    createdAt = createdAt,
    updatedAt = updatedAt
)

internal fun CommentPresetDto.CommentPreset.dtoToVo() = CommentPresetVo(
    presetId = presetId,
    body = body,
    createdAt = createdAt,
    updateAt = updateAt
)