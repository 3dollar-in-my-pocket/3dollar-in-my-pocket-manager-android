package app.threedollars.manager.feature.storemanagement.components.storepost

import java.text.SimpleDateFormat
import java.util.Locale

internal object RelativeTimeFormatter {
    private const val MINUTE = 60L
    private const val HOUR = 60L * MINUTE
    private const val DAY = 24L * HOUR

    fun format(createdAt: String, nowMillis: Long = System.currentTimeMillis()): String {
        val date = runCatching {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(createdAt)
        }.getOrNull() ?: return ""
        val diffSeconds = kotlin.math.abs(nowMillis - date.time) / 1000
        return when {
            diffSeconds < MINUTE -> "방금 전"
            diffSeconds < HOUR -> "${diffSeconds / MINUTE}분 전"
            diffSeconds < DAY -> "${diffSeconds / HOUR}시간 전"
            diffSeconds < DAY * 2 -> "${diffSeconds / DAY}일 전"
            else -> SimpleDateFormat("yyyy년MM월dd일", Locale.getDefault()).format(date)
        }
    }
}
