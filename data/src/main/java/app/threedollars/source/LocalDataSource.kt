package app.threedollars.source

import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun saveAccessToken(token: String): Flow<Unit>

    fun getAccessToken(): Flow<String>
}