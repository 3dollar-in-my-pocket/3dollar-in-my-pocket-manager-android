package app.threedollars.source

import app.threedollars.db.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val dataStoreManager: DataStoreManager) : LocalDataSource {
    override suspend fun saveSocialAccessToken(token: String) = flow {
        emit(dataStoreManager.saveStringData(SOCIAL_ACCESS_TOKEN, token))
    }

    override suspend fun saveAccessToken(token: String) = flow {
        emit(dataStoreManager.saveStringData(ACCESS_TOKEN, token))
    }

    override suspend fun saveVersionName(version: String) = flow {
        emit(dataStoreManager.saveStringData(VERSION_NAME, version))
    }

    override suspend fun saveApplicationId(applicationId: String) = flow {
        emit(dataStoreManager.saveStringData(APPLICATION_ID, applicationId))
    }

    override fun getSocialAccessToken(): Flow<String> = dataStoreManager.getStringData(SOCIAL_ACCESS_TOKEN)
    override fun getAccessToken(): Flow<String> = dataStoreManager.getStringData(ACCESS_TOKEN)


    override fun getVersionName(): Flow<String> = dataStoreManager.getStringData(VERSION_NAME)

    override fun getApplicationId(): Flow<String> = dataStoreManager.getStringData(APPLICATION_ID)

    companion object {
        const val SOCIAL_ACCESS_TOKEN = "social_access_token"
        const val ACCESS_TOKEN = "access_token"
        const val VERSION_NAME = "version_name"
        const val APPLICATION_ID = "application_id"
    }
}