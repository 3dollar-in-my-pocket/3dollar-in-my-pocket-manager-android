package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.repository.CouponRepository
import javax.inject.Inject

class CloseCouponUseCase @Inject constructor(
    private val couponRepository: CouponRepository,
) {
    suspend operator fun invoke(storeId: String, couponId: String): Resource<String> =
        couponRepository.closeCoupon(storeId = storeId, couponId = couponId)
}
