package app.threedollars.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppConfigRepository {
    suspend fun saveVersionName(version: String): Flow<Unit>
    suspend fun saveApplicationId(applicationId: String): Flow<Unit>

    fun isCouponNewBadgeShown(): Flow<Boolean>
    suspend fun saveCouponNewBadgeShown()
    fun isCouponTooltipShown(): Flow<Boolean>
    suspend fun saveCouponTooltipShown()

    fun isMessageMainTabTooltipShown(): Flow<Boolean>
    suspend fun saveMessageMainTabTooltipShown()
    fun isMessageSubTabTooltipShown(): Flow<Boolean>
    suspend fun saveMessageSubTabTooltipShown()
    suspend fun clearMessageTooltipShown()
}
