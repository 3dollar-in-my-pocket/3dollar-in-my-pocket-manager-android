package app.threedollars.source

import app.threedollars.db.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val dataStoreManager: DataStoreManager) : LocalDataSource {
    override suspend fun saveAccessToken(token: String) {
        dataStoreManager.saveStringData(ACCESS_TOKEN, token)
    }

    override fun getAccessToken(): Flow<String> = flow {
        dataStoreManager.getStringData(ACCESS_TOKEN)
    }

    companion object {
        const val ACCESS_TOKEN = "access_token"
    }
}