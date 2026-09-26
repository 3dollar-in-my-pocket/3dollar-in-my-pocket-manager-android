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

    override fun isCouponNewBadgeShown(): Flow<Boolean> = localDataSource.isCouponNewBadgeShown()

    override suspend fun saveCouponNewBadgeShown() = localDataSource.saveCouponNewBadgeShown()

    override fun isCouponTooltipShown(): Flow<Boolean> = localDataSource.isCouponTooltipShown()

    override suspend fun saveCouponTooltipShown() = localDataSource.saveCouponTooltipShown()
}
