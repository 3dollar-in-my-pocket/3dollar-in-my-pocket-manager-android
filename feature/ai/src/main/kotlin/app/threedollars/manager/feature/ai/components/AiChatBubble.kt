package app.threedollars.manager.feature.ai.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.White

@Composable
fun AiChatBubble(
    modifier: Modifier = Modifier,
    fullText: String,
    boldText: String? = null,
) {
    Box(
        modifier = modifier
            .wrapContentSize()
            .background(
                color = Green,
                shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp, bottomEnd = 16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        if (boldText != null && fullText.contains(boldText)) {
            val annotatedString = buildAnnotatedString {
                val startIndex = fullText.indexOf(boldText)
                val endIndex = startIndex + boldText.length

                if (startIndex > 0) {
                    withStyle(
                        style = SpanStyle(
                            color = White,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    ) {
                        append(fullText.substring(0, startIndex))
                    }
                }

                withStyle(
                    style = SpanStyle(
                        color = White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                ) {
                    append(boldText)
                }

                if (endIndex < fullText.length) {
                    withStyle(
                        style = SpanStyle(
                            color = White,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )
                    ) {
                        append(fullText.substring(endIndex))
                    }
                }
            }

            Text(
                text = annotatedString,
                color = White,
            )
        } else {
            Text(
                text = fullText,
                color = White,
            )
        }
    }
}

@Preview("AiChatBubble - Simple Text")
@Composable
private fun AiChatBubblePreview() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        AiChatBubble(
            fullText = "안녕하세요!\n이의동 붕어빵 레전드 사장님,"
        )

        Spacer(modifier = Modifier.height(16.dp))

        AiChatBubble(
            fullText = "2025년 9월 29일 월요일\n오늘의 영업을 도와드릴게요!",
            boldText = "2025년 9월 29일 월요일"
        )
    }
}
