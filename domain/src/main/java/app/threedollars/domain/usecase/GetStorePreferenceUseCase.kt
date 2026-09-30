package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StorePreferenceDto
import app.threedollars.domain.repository.StorePreferenceRepository
import javax.inject.Inject

class GetStorePreferenceUseCase @Inject constructor(
    private val storePreferenceRepository: StorePreferenceRepository,
) {
    suspend operator fun invoke(storeId: String): Resource<StorePreferenceDto> =
        storePreferenceRepository.getStorePreference(storeId = storeId)
}
