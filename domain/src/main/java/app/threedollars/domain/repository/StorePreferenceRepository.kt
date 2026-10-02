package app.threedollars.domain.repository

import app.threedollars.common.Resource
import app.threedollars.domain.dto.StorePreferenceDto

interface StorePreferenceRepository {
    suspend fun getStorePreference(storeId: String): Resource<StorePreferenceDto>

    suspend fun patchStorePreference(storeId: String, preference: StorePreferenceDto): Resource<String>
}
