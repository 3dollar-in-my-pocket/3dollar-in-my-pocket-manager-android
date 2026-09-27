package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.CouponListDto
import app.threedollars.domain.dto.CouponStatus
import app.threedollars.domain.repository.CouponRepository
import javax.inject.Inject

class GetCouponListUseCase @Inject constructor(
    private val couponRepository: CouponRepository,
) {
    suspend operator fun invoke(
        storeId: String,
        statuses: List<CouponStatus>,
        size: Int = 20,
        cursor: String? = null,
    ): Resource<CouponListDto> = couponRepository.getCoupons(storeId = storeId, statuses = statuses, size = size, cursor = cursor)
}
