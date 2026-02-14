package app.threedollars.manager.feature.storemanagement.components.review.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Gray0
import app.threedollars.common.ui.Gray50
import app.threedollars.common.ui.Gray95
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.MainPink
import app.threedollars.common.ui.Pink100
import app.threedollars.common.ui.Pink200
import app.threedollars.common.ui.White
import app.threedollars.manager.feature.storemanagement.R
import app.threedollars.manager.feature.storemanagement.ScreenType
import app.threedollars.manager.feature.storemanagement.model.FeedbackFullVo
import app.threedollars.manager.feature.storemanagement.model.FeedbackTypesVo


@Composable
internal fun FeedbackCardView(
    feedbackFulls: List<FeedbackFullVo>,
    feedbackTypes: List<FeedbackTypesVo>,
    onScreenTypeUpdate: (ScreenType) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = White
        )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.align(
                        Alignment.TopStart
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "피드백 평가",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Green
                    )
                }

                Row(
                    modifier = Modifier
                        .align(
                            Alignment.TopEnd
                        )
                        .clickable {
                            onScreenTypeUpdate(ScreenType.FEEDBACK)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier,
                        text = "전체 보기",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Gray50
                    )
                    Image(
                        modifier = Modifier.padding(start = 4.dp),
                        imageVector = ImageVector.vectorResource(
                            id = R.drawable.ic_arrow_right,
                        ),
                        contentDescription = ""
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    val text = buildAnnotatedString {
                        withStyle(
                            style =
                            SpanStyle(
                                color = Green,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp
                            )
                        ) {
                            append("${(feedbackFulls.sumOf { it.count })}개")
                        }
                        append("의 피드백 평가")
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 30.dp
                            ),
                        text = text,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Gray95
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    feedbackFulls.sortedByDescending { it.count }.take(3).forEach { feedback ->
                        val feedbackType = feedbackTypes.find {
                            it.feedbackType == feedback.feedbackType
                        }
                        feedback.count
                        if (feedbackType != null) {
                            FeedbackCardView(
                                feedbackDescription = feedbackType.description,
                                feedbackEmoji = feedbackType.emoji,
                                feedbackCount = feedback.count
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeedbackCardView(
    feedbackDescription: String,
    feedbackEmoji: String,
    feedbackCount: Int
) {
    Card(
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = Gray0
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = feedbackEmoji)
            Text(
                text = feedbackDescription,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Gray95
            )
            Text(
                text = "${feedbackCount}개",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Gray50
            )
        }
    }
}
