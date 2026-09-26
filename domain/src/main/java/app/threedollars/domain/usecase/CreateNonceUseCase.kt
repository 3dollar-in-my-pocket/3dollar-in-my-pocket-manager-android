package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.repository.CouponRepository
import javax.inject.Inject

class CreateNonceUseCase @Inject constructor(
    private val couponRepository: CouponRepository,
) {
    suspend operator fun invoke(): Resource<String> = couponRepository.createNonce()
}
