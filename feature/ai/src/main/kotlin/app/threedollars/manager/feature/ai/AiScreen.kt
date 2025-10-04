package app.threedollars.manager.feature.ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.CircleProgressBar
import app.threedollars.manager.feature.ai.components.AiChatBubble
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
internal fun AiScreen(
    uiState: AiState,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircleProgressBar()
                }
            }
            uiState.isError -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.errorMessage,
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = app.threedollars.common.R.color.green500)
                        )
                    ) {
                        Text(
                            text = "새로고침",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            else -> {
                MarkdownText(
                    markdown = uiState.recommendationText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                )
            }
        }
    }
}
