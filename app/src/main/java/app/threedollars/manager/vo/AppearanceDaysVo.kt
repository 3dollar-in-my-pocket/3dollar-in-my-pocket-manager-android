package app.threedollars.manager.vo


data class AppearanceDaysVo(
    val dayOfTheWeek: String,
    val openingHours: OpeningHoursVo,
    val locationDescription: String
)