package app.threedollars.manager.feature.storemanagement.components.review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import app.threedollars.domain.dto.ContentsDto
import app.threedollars.manager.feature.storemanagement.components.review.component.BookmarkCardView
import app.threedollars.manager.feature.storemanagement.model.FeedbackFullVo
import app.threedollars.manager.feature.storemanagement.model.FeedbackTypesVo

@Composable
internal fun ReviewScreen(
    subscriberCount: Int,
    feedbackFulls: List<FeedbackFullVo>,
    feedbackTypes: List<FeedbackTypesVo>,
    feedbackSpecific: LazyPagingItems<ContentsDto>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 24.dp
            )
    ) {
        BookmarkCardView(
            subscriberCount = subscriberCount
        )
    }
}