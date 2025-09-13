package app.threedollars.manager.feature.storemanagement.components.review.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Gray40
import app.threedollars.common.ui.Gray50
import app.threedollars.common.ui.Gray80
import app.threedollars.manager.feature.storemanagement.formatDate
import app.threedollars.manager.feature.storemanagement.model.ReviewVo

@Composable
internal fun BlindReviewCardView(
    reviewVo: ReviewVo,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 12.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = reviewVo.writer.name,
                fontSize = 12.sp,
                color = Gray80,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = reviewVo.createdAt.formatDate(),
                fontSize = 12.sp,
                color = Gray40,
                fontWeight = FontWeight.Medium,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "규정 위반으로 블라인드 처리되었습니다.",
                fontSize = 14.sp,
                color = Gray50,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Preview("Preview")
@Composable
private fun Preview() {
    BlindReviewCardView(
        reviewVo = ReviewVo(
            rating = 4,
            contents = "맛있어요!!",
            writer = ReviewVo.Writer(name = "관악구 광화문연가"),
            createdAt = "2025-02-03T16:57:56",
            sticker = ReviewVo.Sticker(),
            comment = ReviewVo.Comment()
        ),
    )
}