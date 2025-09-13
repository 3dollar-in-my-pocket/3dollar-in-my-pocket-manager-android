package app.threedollars.common.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext


/**
 * 뒤로가기 버튼을 두 번 누르면 앱을 종료시키는 Composable 핸들러.
 *
 * @param intervalMs 뒤로가기 간격 (기본 3초)
 * @param message 첫 번째 뒤로가기 시 토스트로 보여줄 메시지
 */
@Composable
fun DoubleBackExitHandler(
    intervalMs: Long = 3000L,
    message: String = "한번 더 누르면 앱이 종료됩니다.",
) {
    val context = LocalContext.current
    val lastBackTime = rememberSaveable { mutableLongStateOf(0L) }

    BackHandler {
        val now = System.currentTimeMillis()
        if (now - lastBackTime.longValue > intervalMs) {
            // 첫 번째 뒤로가기
            lastBackTime.longValue = now
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } else {
            // 두 번째 뒤로가기: 앱 종료
            (context as? Activity)?.finishAndRemoveTask()
        }
    }
}