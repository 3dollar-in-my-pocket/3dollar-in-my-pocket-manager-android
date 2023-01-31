package app.threedollars.domain.dto


data class AppearanceDaysRequestDto(
    val dayOfTheWeek: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val locationDescription: String? = null
)