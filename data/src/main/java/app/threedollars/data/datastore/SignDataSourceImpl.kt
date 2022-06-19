package app.threedollars.data.datastore

import app.threedollars.data.db.DataStoreHelper
import app.threedollars.data.user.UserService
import javax.inject.Inject

class SignDataSourceImpl @Inject constructor(
    private val service: UserService,
    private val dataStoreHelper: DataStoreHelper
) : SignDataSource {

    override suspend fun loginWithKakao() {

    }

    override suspend fun saveAccessToken() {

    }

    override suspend fun getAccessToken() {

    }

    override suspend fun loginToManagerServer() {

    }

    override suspend fun signUpToManagerServer() {

    }

    override suspend fun logout() {

    }

    override suspend fun deleteMyAccount() {

    }
}