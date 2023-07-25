package app.threedollars.source

import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun saveSocialAccessToken(token: String): Flow<Unit>
    suspend fun saveAccessToken(token: String): Flow<Unit>
    suspend fun saveVersionName(version: String): Flow<Unit>
    suspend fun saveApplicationId(applicationId: String): Flow<Unit>

    fun getSocialAccessToken(): Flow<String>
    fun getAccessToken(): Flow<String>
    fun getVersionName(): Flow<String>
    fun getApplicationId(): Flow<String>
}