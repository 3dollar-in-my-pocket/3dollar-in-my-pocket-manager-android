package app.threedollars.domain.usecase

import app.threedollars.domain.repository.AppConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageGuideUseCase @Inject constructor(
    private val appConfigRepository: AppConfigRepository,
) {
    fun isMainTabTooltipShown(): Flow<Boolean> = appConfigRepository.isMessageMainTabTooltipShown()

    suspend fun markMainTabTooltipShown() = appConfigRepository.saveMessageMainTabTooltipShown()

    fun isSubTabTooltipShown(): Flow<Boolean> = appConfigRepository.isMessageSubTabTooltipShown()

    suspend fun markSubTabTooltipShown() = appConfigRepository.saveMessageSubTabTooltipShown()

    suspend fun reset() = appConfigRepository.clearMessageTooltipShown()
}
