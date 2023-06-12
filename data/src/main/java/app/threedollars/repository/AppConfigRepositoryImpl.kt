package app.threedollars.repository

import app.threedollars.domain.repository.AppConfigRepository
import app.threedollars.source.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppConfigRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : AppConfigRepository {
    override suspend fun saveVersionName(version: String): Flow<Unit> = localDataSource.saveVersionName(version)

    override suspend fun saveApplicationId(applicationId: String): Flow<Unit> = localDataSource.saveApplicationId(applicationId)
}