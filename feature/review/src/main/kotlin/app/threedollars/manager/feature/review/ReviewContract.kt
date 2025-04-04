package app.threedollars.manager.feature.review

import app.threedollars.data.response.CommentPresetResponse
import app.threedollars.manager.feature.review.model.BossStoreRetrieveVo
import app.threedollars.manager.feature.review.model.CommentPresetVo
import app.threedollars.manager.feature.review.model.ReviewVo


internal data class ReviewState(
    val screenType: ScreenType = ScreenType.ALL_REVIEW,
    val dialogType: DialogType = DialogType.NONE,
    val errorMessage: String? = null,
    val bossStoreRetrieve: BossStoreRetrieveVo = BossStoreRetrieveVo(),
    val reviews: List<ReviewVo> = listOf(),
    val reviewFilterType: ReviewFilterType = ReviewFilterType.LATEST,
    val selectedReview: ReviewVo = ReviewVo(),
    val commentPresets: List<CommentPresetVo> = listOf(),
    val selectEditPresetText: String = "",
    val selectEditPresetId: String = ""
)

internal enum class ScreenType {
    ALL_REVIEW,
    REVIEW_DETAIL,
}

internal enum class DialogType {
    NONE,
    ERROR_DIALOG,
    REPORT_DIALOG,
    PRESET_DIALOG,
    PRESET_WRITE_DIALOG,
    PRESET_EDIT_DIALOG
}

internal enum class ReviewFilterType(val title: String) {
    LATEST(title = "최신순"),
    HIGHEST_RATING(title = "별점 높은순"),
    LOWEST_RATING(title = "별점 낮은순")
}