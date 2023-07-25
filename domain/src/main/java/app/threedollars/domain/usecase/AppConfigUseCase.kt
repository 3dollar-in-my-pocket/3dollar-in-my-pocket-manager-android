package app.threedollars.domain.usecase

import kotlinx.coroutines.flow.Flow

interface AppConfigUseCase {
    suspend fun saveVersionName(version: String): Flow<Unit>
    suspend fun saveApplicationId(applicationId: String): Flow<Unit>
}