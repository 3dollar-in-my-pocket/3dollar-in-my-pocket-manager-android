package app.threedollars.dto


data class AppearanceDaysRequestDto(
    val dayOfTheWeek: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val locationDescription: String? = null
)