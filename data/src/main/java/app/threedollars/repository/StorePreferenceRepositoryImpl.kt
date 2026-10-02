package app.threedollars.repository

import app.threedollars.common.Resource
import app.threedollars.common.ext.toStringDefault
import app.threedollars.data.request.StorePreferenceRequest
import app.threedollars.domain.dto.StorePreferenceDto
import app.threedollars.domain.repository.StorePreferenceRepository
import app.threedollars.source.RemoteDataSource
import javax.inject.Inject

internal class StorePreferenceRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : StorePreferenceRepository {

    override suspend fun getStorePreference(storeId: String): Resource<StorePreferenceDto> = runCatching {
        remoteDataSource.getStorePreference(storeId = storeId)
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = it.data!!.toDto(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = { Resource.Error(errorMessage = it.message, code = "700") }
    )

    override suspend fun patchStorePreference(storeId: String, preference: StorePreferenceDto): Resource<String> = runCatching {
        remoteDataSource.patchStorePreference(storeId = storeId, request = StorePreferenceRequest.from(preference))
    }.fold(
        onSuccess = {
            if (it.data != null) {
                Resource.Success(data = it.data.toStringDefault(), code = it.code)
            } else {
                Resource.Error(errorMessage = it.errorMessage, code = it.code)
            }
        },
        onFailure = { Resource.Error(errorMessage = it.message, code = "700") }
    )
}
