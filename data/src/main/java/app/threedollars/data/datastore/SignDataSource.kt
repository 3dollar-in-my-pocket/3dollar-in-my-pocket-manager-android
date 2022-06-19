package app.threedollars.data.datastore

interface SignDataSource {

    suspend fun loginWithKakao()

    suspend fun saveAccessToken()

    suspend fun getAccessToken()

    suspend fun loginToManagerServer()

    suspend fun signUpToManagerServer()

    suspend fun logout()

    suspend fun deleteMyAccount()
}