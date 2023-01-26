package app.threedollars.dto


data class AppearanceDaysDto(
    val dayOfTheWeek: String? = null,
    val openingHours: OpeningHoursDto? = null,
    val locationDescription: String? = null
)