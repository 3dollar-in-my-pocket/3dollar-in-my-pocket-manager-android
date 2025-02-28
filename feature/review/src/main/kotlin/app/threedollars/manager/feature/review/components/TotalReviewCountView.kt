package app.threedollars.manager.feature.review.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Gray90
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.Green100
import app.threedollars.manager.feature.review.R

@Composable
internal fun TotalReviewCountView(
    reviewTotalCount: Int,
    rating: Double
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "리뷰",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )

        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = "${reviewTotalCount}개",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Green
        )

        Card(
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = 8.dp),
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = Green100
            )
        ) {
            Row(
                modifier = Modifier.padding(
                    vertical = 4.dp,
                    horizontal = 6.dp
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_star_solid_14),
                    tint = Green,
                    contentDescription = ""
                )

                Text(
                    text = rating.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray90
                )
            }
        }
    }
}