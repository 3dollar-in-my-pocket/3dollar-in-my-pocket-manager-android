package app.threedollars.manager.feature.storemanagement.components.review.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Gray50
import app.threedollars.common.ui.Gray95
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.White
import app.threedollars.manager.feature.storemanagement.R

@Composable
internal fun BookmarkCardView(
    subscriberCount: Int
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
                    Image(
                        imageVector = ImageVector.vectorResource(
                            id = R.drawable.ic_bookmark,
                        ),
                        contentDescription = ""
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp),
                        text = "북마크",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Green
                    )
                }

                val text = buildAnnotatedString {
                    withStyle(
                        style =
                        SpanStyle(
                            color = Green,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        )
                    ) {
                        append("${subscriberCount}명")
                    }
                    append("의 고객이 사장님의 가게를 북마크 했습니다!")
                }
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(
                            top = 30.dp
                        ),
                    text = text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Gray95
                )
            }
        }
    }
}