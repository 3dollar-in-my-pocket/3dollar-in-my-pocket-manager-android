package app.threedollars.manager.usecase

import app.threedollars.domain.repository.AppConfigRepository
import app.threedollars.domain.usecase.AppConfigUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppConfigUseCaseImpl @Inject constructor(private val appConfigRepository: AppConfigRepository) : AppConfigUseCase {
    override suspend fun saveVersionName(version: String): Flow<Unit> = appConfigRepository.saveVersionName(version)

    override suspend fun saveApplicationId(applicationId: String): Flow<Unit> = appConfigRepository.saveApplicationId(applicationId)
}