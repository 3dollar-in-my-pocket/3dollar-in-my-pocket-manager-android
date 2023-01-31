package app.threedollars.domain.dto


data class AppearanceDaysDto(
    val dayOfTheWeek: String? = null,
    val openingHours: OpeningHoursDto? = null,
    val locationDescription: String? = null
)