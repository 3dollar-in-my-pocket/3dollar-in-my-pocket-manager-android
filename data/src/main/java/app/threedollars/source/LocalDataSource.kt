package app.threedollars.source

import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun saveSocialAccessToken(token: String): Flow<Unit>
    suspend fun saveAccessToken(token: String): Flow<Unit>

    fun getSocialAccessToken(): Flow<String>
    fun getAccessToken(): Flow<String>
}