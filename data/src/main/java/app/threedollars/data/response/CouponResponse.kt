package app.threedollars.data.response

import app.threedollars.common.ext.toStringDefault
import app.threedollars.data.model.CursorModel
import app.threedollars.domain.dto.CouponDto
import app.threedollars.domain.dto.CouponListDto
import app.threedollars.domain.dto.CouponStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CouponListResponse(
    @SerialName("contents")
    val contents: List<CouponResponse> = listOf(),
    @SerialName("cursor")
    val cursor: CursorModel? = CursorModel(),
) {
    fun toDto() = CouponListDto(
        contents = contents.map { it.toDto() },
        cursor = cursor?.toDto(),
    )
}

@Serializable
internal data class CouponResponse(
    @SerialName("couponId")
    val couponId: String = "",
    @SerialName("name")
    val name: String? = null,
    @SerialName("maxIssuableCount")
    val maxIssuableCount: Int = 0,
    @SerialName("currentIssuedCount")
    val currentIssuedCount: Int = 0,
    @SerialName("currentUsedCount")
    val currentUsedCount: Int = 0,
    @SerialName("validityPeriod")
    val validityPeriod: ValidityPeriod? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("updatedAt")
    val updatedAt: String? = null,
) {
    @Serializable
    internal data class ValidityPeriod(
        @SerialName("startDateTime")
        val startDateTime: String? = null,
        @SerialName("endDateTime")
        val endDateTime: String? = null,
    ) {
        fun toDto() = CouponDto.ValidityPeriod(
            startDateTime = startDateTime.toStringDefault(),
            endDateTime = endDateTime.toStringDefault(),
        )
    }

    fun toDto() = CouponDto(
        couponId = couponId,
        name = name.toStringDefault(),
        maxIssuableCount = maxIssuableCount,
        currentIssuedCount = currentIssuedCount,
        currentUsedCount = currentUsedCount,
        validityPeriod = validityPeriod?.toDto() ?: CouponDto.ValidityPeriod(),
        status = CouponStatus.from(status),
        createdAt = createdAt.toStringDefault(),
        updatedAt = updatedAt.toStringDefault(),
    )
}
