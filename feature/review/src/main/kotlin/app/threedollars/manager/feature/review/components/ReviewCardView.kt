package app.threedollars.manager.feature.review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Gray0
import app.threedollars.common.ui.Gray40
import app.threedollars.common.ui.Gray60
import app.threedollars.common.ui.Gray80
import app.threedollars.common.ui.MainPink
import app.threedollars.common.ui.Pink100
import app.threedollars.common.ui.Pink200
import app.threedollars.manager.feature.review.R
import app.threedollars.manager.feature.review.formatDate
import app.threedollars.manager.feature.review.model.ReviewVo
import coil.compose.AsyncImage

@Composable
internal fun ReviewCardView(
    reviewVo: ReviewVo,
    onReviewDetailClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .clickable { onReviewDetailClick(reviewVo.reviewId) }
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

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MedalCardView(
                medal = reviewVo.writer.medal
            )
            RatingCardView(
                rating = reviewVo.rating
            )
        }

        if (reviewVo.images.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                items(reviewVo.images) { image ->
                    Card(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(96.dp)
                    ) {
                        AsyncImage(
                            model = image.imageUrl,
                            contentDescription = "Review Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
        }

        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = reviewVo.contents,
            fontSize = 14.sp,
            color = Gray80,
            fontWeight = FontWeight.Normal
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 13.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_heart_line),
                contentDescription = ""
            )
            Text(
                text = "좋아요 ${reviewVo.sticker.count}",
                fontSize = 10.sp,
                color = Gray60,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        reviewVo.comment?.let { comment ->
            CommentCardView(comment)
        }
    }
}

@Composable
private fun MedalCardView(medal: ReviewVo.Writer.Medal) {
    Card(
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = Pink100
        )
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            AsyncImage(
                modifier = Modifier.size(16.dp),
                model = medal.iconUrl,
                contentDescription = "Medal",
            )

            Text(
                text = medal.name,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = MainPink
            )
        }
    }
}

@Composable
private fun RatingCardView(rating: Int) {
    Card(
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = Pink100
        )
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            repeat(times = 5) { index ->
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_star_solid_12),
                    tint = if ((rating - 1) < index) Pink200 else MainPink,
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
private fun CommentCardView(
    comment: ReviewVo.Comment
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = Gray0
        )
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 12.dp,
                horizontal = 16.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.commentId,
                    fontSize = 12.sp,
                    color = Gray80,
                    fontWeight = FontWeight.Medium,
                )
                comment.createdAt?.let { createdAt ->
                    Text(
                        text = createdAt.formatDate(),
                        fontSize = 12.sp,
                        color = Gray40,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = comment.content,
                fontSize = 14.sp,
                color = Gray80,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Preview("ReviewCardViewPreview")
@Composable
private fun ReviewCardViewPreview() {
    ReviewCardView(
        reviewVo = ReviewVo(
            rating = 4,
            contents = "맛있어요!!",
            writer = ReviewVo.Writer(name = "관악구 광화문연가"),
            createdAt = "2025-02-03T16:57:56",
            sticker = ReviewVo.Sticker(),
            comment = ReviewVo.Comment()
        ),
        onReviewDetailClick = {}
    )
}