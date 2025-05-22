package app.threedollars.manager.feature.storemanagement.components.review.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.White
import app.threedollars.manager.feature.storemanagement.model.ReviewVo

@Composable
internal fun ReviewListView(
    reviewTotalCount: Int,
    rating: Double,
    reviews: List<ReviewVo>,
    storeName: String,
    onAllReviewNavigate: (String?) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
    ) {
        TotalReviewCountView(
            reviewTotalCount = reviewTotalCount,
            rating = rating
        )

        if (reviews.isEmpty()) {
            // TODO: 리스트뷰가 빈값일 때 처리
        } else {
            reviews.forEach { reviewVo ->
                Spacer(modifier = Modifier.height(20.dp))

                if (reviewVo.status == "FILTERED") {
                    BlindReviewCardView(reviewVo = reviewVo)
                } else {
                    ReviewCardView(
                        reviewVo = reviewVo,
                        storeName = storeName,
                        onReviewDetailClick = onAllReviewNavigate
                    )
                }
            }

            Button(
                onClick = {
                    onAllReviewNavigate(null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green
                )
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "리뷰 전체 보기",
                    fontSize = 14.sp,
                    color = White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

