package app.threedollars.repository

import app.threedollars.common.Resource
import app.threedollars.domain.dto.AppStatusDto
import app.threedollars.domain.repository.AppRepository
import app.threedollars.source.RemoteDataSource
import javax.inject.Inject

internal class AppRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) : AppRepository {

    override suspend fun getAppStatus(): Resource<AppStatusDto> = runCatching {
        remoteDataSource.getAppStatus()
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
}
