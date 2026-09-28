package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.CouponDto
import app.threedollars.domain.dto.CouponRegisterDto
import app.threedollars.domain.repository.CouponRepository
import javax.inject.Inject

class RegisterCouponUseCase @Inject constructor(
    private val couponRepository: CouponRepository,
) {
    suspend operator fun invoke(storeId: String, nonce: String, request: CouponRegisterDto): Resource<CouponDto> =
        couponRepository.registerCoupon(storeId = storeId, nonce = nonce, request = request)
}
