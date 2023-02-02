package app.threedollars.manager.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.LoginDto
import app.threedollars.domain.repository.UserRepository
import app.threedollars.domain.usecase.AuthUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthUseCaseImpl @Inject constructor(private val userRepository: UserRepository) : AuthUseCase {
    override fun login(socialType: String, token: String): Flow<Resource<LoginDto>> =
        userRepository.login(socialType, token)

    override fun logout(): Flow<Resource<String>> =
        userRepository.logout()

    override fun signUp(
        bossName: String,
        businessNumber: String,
        certificationPhotoUrl: String,
        socialType: String,
        storeCategoriesIds: List<String>,
        storeName: String,
        token: String
    ): Flow<Resource<String>> =
        userRepository.signUp(bossName, businessNumber, certificationPhotoUrl, socialType, storeCategoriesIds, storeName, token)

    override fun signOut(): Flow<Resource<String>> =
        userRepository.signOut()
}