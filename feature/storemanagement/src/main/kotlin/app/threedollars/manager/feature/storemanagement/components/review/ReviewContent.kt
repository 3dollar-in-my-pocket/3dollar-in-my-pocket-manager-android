package app.threedollars.manager.feature.storemanagement.components.review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.threedollars.manager.feature.storemanagement.ScreenType
import app.threedollars.manager.feature.storemanagement.components.review.component.BookmarkCardView
import app.threedollars.manager.feature.storemanagement.components.review.component.FeedbackCardView
import app.threedollars.manager.feature.storemanagement.components.review.component.ReviewListView
import app.threedollars.manager.feature.storemanagement.model.FeedbackFullVo
import app.threedollars.manager.feature.storemanagement.model.FeedbackTypesVo
import app.threedollars.manager.feature.storemanagement.model.ReviewVo

@Composable
internal fun ReviewContent(
    subscriberCount: Int,
    rating: Double,
    reviewTotalCount: Int,
    reviews: List<ReviewVo>,
    feedbackFulls: List<FeedbackFullVo>,
    feedbackTypes: List<FeedbackTypesVo>,
    onScreenTypeUpdate: (ScreenType) -> Unit,
    onAllReviewNavigate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        BookmarkCardView(
            subscriberCount = subscriberCount
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        FeedbackCardView(
            feedbackFulls = feedbackFulls,
            feedbackTypes = feedbackTypes,
            onScreenTypeUpdate = onScreenTypeUpdate
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        ReviewListView(
            reviewTotalCount = reviewTotalCount,
            rating = rating,
            reviews = reviews,
            onAllReviewNavigate = onAllReviewNavigate,
        )
    }
}