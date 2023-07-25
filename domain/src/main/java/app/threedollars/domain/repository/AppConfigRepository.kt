package app.threedollars.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppConfigRepository {
    suspend fun saveVersionName(version: String): Flow<Unit>
    suspend fun saveApplicationId(applicationId: String): Flow<Unit>
}