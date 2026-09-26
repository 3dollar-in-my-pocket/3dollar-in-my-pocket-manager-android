package app.threedollars.manager.feature.storemanagement.components.coupon

import app.threedollars.domain.dto.CouponRegisterDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

internal object CouponFormatter {
    private val serverDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    private val periodDisplay = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    private val pickerDisplay = DateTimeFormatter.ofPattern("yyyy. M. d EEEE", Locale.KOREAN)

    fun formatPeriod(startDateTime: String, endDateTime: String): String =
        "${formatServerDate(startDateTime)} ~ ${formatServerDate(endDateTime)}"

    fun formatServerDate(dateTime: String): String = runCatching {
        LocalDateTime.parse(dateTime, serverDateTime).format(periodDisplay)
    }.getOrDefault(dateTime)

    fun formatPickerDate(date: LocalDate): String = date.format(pickerDisplay)

    fun toStartDateTime(date: LocalDate): String = date.atStartOfDay().format(serverDateTime)

    fun toEndDateTime(date: LocalDate): String = date.atTime(23, 59, 59).format(serverDateTime)

    fun toRegisterDto(state: RegisterCouponState): CouponRegisterDto? {
        val count = state.maxIssuableCount ?: return null
        return CouponRegisterDto(
            name = state.name,
            description = null,
            maxIssuableCount = count,
            startDateTime = toStartDateTime(state.startDate),
            endDateTime = toEndDateTime(state.endDate),
        )
    }
}
