package app.threedollars.domain.usecase

import app.threedollars.domain.repository.AppConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CouponGuideUseCase @Inject constructor(
    private val appConfigRepository: AppConfigRepository,
) {
    fun isNewBadgeShown(): Flow<Boolean> = appConfigRepository.isCouponNewBadgeShown()

    suspend fun markNewBadgeShown() = appConfigRepository.saveCouponNewBadgeShown()

    fun isTooltipShown(): Flow<Boolean> = appConfigRepository.isCouponTooltipShown()

    suspend fun markTooltipShown() = appConfigRepository.saveCouponTooltipShown()
}
