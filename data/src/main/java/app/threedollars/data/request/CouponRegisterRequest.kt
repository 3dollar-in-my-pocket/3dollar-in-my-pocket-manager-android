package app.threedollars.data.request

import app.threedollars.domain.dto.CouponRegisterDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CouponRegisterRequest(
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("maxIssuableCount")
    val maxIssuableCount: Int,
    @SerialName("validityPeriod")
    val validityPeriod: ValidityPeriod,
) {
    @Serializable
    internal data class ValidityPeriod(
        @SerialName("startDateTime")
        val startDateTime: String,
        @SerialName("endDateTime")
        val endDateTime: String,
    )

    companion object {
        fun from(dto: CouponRegisterDto) = CouponRegisterRequest(
            name = dto.name,
            description = dto.description,
            maxIssuableCount = dto.maxIssuableCount,
            validityPeriod = ValidityPeriod(startDateTime = dto.startDateTime, endDateTime = dto.endDateTime),
        )
    }
}
