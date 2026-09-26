package app.threedollars.domain.dto

data class CouponListDto(
    val contents: List<CouponDto> = listOf(),
    val cursor: CursorDto? = CursorDto(),
)

data class CouponDto(
    val couponId: String = "",
    val name: String = "",
    val maxIssuableCount: Int = 0,
    val currentIssuedCount: Int = 0,
    val currentUsedCount: Int = 0,
    val validityPeriod: ValidityPeriod = ValidityPeriod(),
    val status: CouponStatus = CouponStatus.UNKNOWN,
    val createdAt: String = "",
    val updatedAt: String = "",
) {
    data class ValidityPeriod(
        val startDateTime: String = "",
        val endDateTime: String = "",
    )
}

enum class CouponStatus {
    ACTIVE,
    STOPPED,
    ENDED,
    UNKNOWN;

    companion object {
        fun from(value: String?): CouponStatus = entries.firstOrNull { it.name == value } ?: UNKNOWN
    }
}

data class CouponRegisterDto(
    val name: String,
    val description: String? = null,
    val maxIssuableCount: Int,
    val startDateTime: String,
    val endDateTime: String,
)
