package app.threedollars.manager.feature.ai

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.CircleProgressBar
import app.threedollars.manager.feature.ai.components.AiChatBubble

@Composable
internal fun AiScreen(
    uiState: AiState
) {
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        AiChatBubble(
            fullText = "안녕하세요!\n${uiState.userName} 사장님,"
        )

        Spacer(modifier = Modifier.height(12.dp))

        AiChatBubble(
            fullText = "${uiState.currentDate}\n오늘의 영업을 도와드릴게요!",
            boldText = uiState.currentDate.takeIf { it.isNotEmpty() }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircleProgressBar()
            } else {
                Text(
                    text = uiState.recommendationText,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    ),
                )
            }
        }
    }
}
