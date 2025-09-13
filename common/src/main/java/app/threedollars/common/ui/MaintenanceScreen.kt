package app.threedollars.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.R

@Composable
fun MaintenanceScreen(
    onRetry: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.95f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // 점검 아이콘
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = Gray50,
                strokeWidth = 4.dp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 메인 타이틀
            Text(
                text = stringResource(R.string.maintenance_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Gray90,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 설명 메시지
            Text(
                text = stringResource(R.string.maintenance_message),
                fontSize = 16.sp,
                color = Gray70,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 로딩 인디케이터
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = Green,
                strokeWidth = 3.dp
            )

            // 재시도 버튼 (선택적)
            onRetry?.let {
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = it,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green,
                        contentColor = White
                    ),
                    modifier = Modifier.padding(horizontal = 48.dp)
                ) {
                    Text(
                        text = stringResource(R.string.maintenance_retry),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}