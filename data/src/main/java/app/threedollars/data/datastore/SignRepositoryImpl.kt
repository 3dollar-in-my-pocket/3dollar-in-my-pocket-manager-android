package app.threedollars.data.datastore

import app.threedollars.domain.repository.SignRepository
import javax.inject.Inject

class SignRepositoryImpl @Inject constructor(
    private val signDataSource: SignDataSource
) : SignRepository {

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