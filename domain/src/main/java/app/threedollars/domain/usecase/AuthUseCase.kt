package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.LoginDto
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {

    suspend fun saveSocialAccessToken(token: String) : Flow<Unit>
    suspend fun saveAccessToken(token: String) : Flow<Unit>

    fun getSocialAccessToken(): Flow<Resource<String>>
    fun getAccessToken(): Flow<Resource<String>>

    fun login(socialType: String, token: String): Flow<Resource<LoginDto>>

    fun logout(): Flow<Resource<String>>

    fun signUp(
        bossName: String,
        businessNumber: String,
        certificationPhotoUrl: String,
        socialType: String,
        storeCategoriesIds: List<String>,
        storeName: String,
        token: String
    ): Flow<Resource<LoginDto>>

    fun signOut(): Flow<Resource<String>>

}