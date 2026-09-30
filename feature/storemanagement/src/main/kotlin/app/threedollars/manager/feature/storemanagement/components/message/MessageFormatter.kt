package app.threedollars.manager.feature.storemanagement.components.message

import java.time.Duration
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

internal object MessageFormatter {
    private val TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.KOREAN)
    private val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd E", Locale.KOREAN)
    private val DAY: Duration = Duration.ofHours(24)

    fun parse(value: String, zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime? {
        if (value.isBlank()) return null
        return runCatching { LocalDateTime.parse(value) }.getOrNull()
            ?: runCatching { OffsetDateTime.parse(value).atZoneSameInstant(zoneId).toLocalDateTime() }.getOrNull()
    }

    fun formatSentAt(createdAt: String, now: LocalDateTime = LocalDateTime.now()): String {
        val sentAt = parse(createdAt) ?: return ""
        return if (Duration.between(sentAt, now) < DAY) sentAt.format(TIME_FORMAT) else sentAt.format(DATE_FORMAT)
    }

    fun formatRemaining(remaining: Duration): String {
        val totalSeconds = remaining.seconds.coerceAtLeast(0)
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
    }
}
