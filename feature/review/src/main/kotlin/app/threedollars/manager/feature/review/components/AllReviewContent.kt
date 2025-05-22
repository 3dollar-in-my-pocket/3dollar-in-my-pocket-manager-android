package app.threedollars.manager.feature.review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Gray40
import app.threedollars.common.ui.Gray5
import app.threedollars.manager.feature.review.R
import app.threedollars.manager.feature.review.ReviewFilterType
import app.threedollars.manager.feature.review.model.ReviewVo
import app.threedollars.manager.feature.review.noRippleClickable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun AllReviewContent(
    reviewTotalCount: Int,
    rating: Double,
    reviewFilterType: ReviewFilterType,
    coroutineScope: CoroutineScope,
    listState: LazyListState,
    onReviewFilterTypeUpdate: (ReviewFilterType) -> Unit,
    storeReviewPaging: LazyPagingItems<ReviewVo>,
    onReviewDetailClick: (String) -> Unit,
    onStoreManagementNavigate: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AllReviewTopBar(
                onStoreManagementNavigate = onStoreManagementNavigate
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TotalReviewCountView(
                reviewTotalCount = reviewTotalCount,
                rating = rating
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Gray5)
            )

            ReviewFilterView(
                reviewFilterType = reviewFilterType,
                onReviewFilterTypeUpdate = { reviewFilterType ->
                    coroutineScope.launch {
                        listState.scrollToItem(0)
                    }
                    onReviewFilterTypeUpdate(reviewFilterType)
                }
            )

            if (storeReviewPaging.itemCount != 0) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    items(storeReviewPaging.itemCount) { index ->
                        val reviewVo = storeReviewPaging[index] ?: ReviewVo()
                        if (reviewVo.status == "FILTERED") {
                            BlindReviewCardView(reviewVo = reviewVo)
                        } else {
                            ReviewCardView(
                                reviewVo = reviewVo,
                                onReviewDetailClick = onReviewDetailClick
                            )
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .background(Gray5)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewFilterView(
    reviewFilterType: ReviewFilterType,
    onReviewFilterTypeUpdate: (ReviewFilterType) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ReviewFilterType.entries.forEach { type ->
            Column {
                Text(
                    text = type.title,
                    fontSize = 12.sp,
                    color = if (reviewFilterType == type) Gray100 else Gray40,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable {
                            onReviewFilterTypeUpdate(type)
                        }
                        .drawBehind {
                            if (reviewFilterType == type) {
                                val strokeWidth = 3f

                                drawLine(
                                    color = Gray100,
                                    start = androidx.compose.ui.geometry.Offset(
                                        0f,
                                        size.height + 8f
                                    ),
                                    end = androidx.compose.ui.geometry.Offset(
                                        size.width,
                                        size.height + 8f
                                    ),
                                    strokeWidth = strokeWidth
                                )
                            }
                        }
                )
            }
        }
    }
}

@Composable
private fun AllReviewTopBar(onStoreManagementNavigate: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 34.dp
            )
    ) {
        Image(
            modifier = Modifier
                .noRippleClickable { onStoreManagementNavigate() },
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "리뷰",
            color = Gray100,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}