package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StorePreferenceDto
import app.threedollars.domain.repository.StorePreferenceRepository
import javax.inject.Inject

class PatchStorePreferenceUseCase @Inject constructor(
    private val storePreferenceRepository: StorePreferenceRepository,
) {
    suspend operator fun invoke(storeId: String, preference: StorePreferenceDto): Resource<String> =
        storePreferenceRepository.patchStorePreference(storeId = storeId, preference = preference)
}
