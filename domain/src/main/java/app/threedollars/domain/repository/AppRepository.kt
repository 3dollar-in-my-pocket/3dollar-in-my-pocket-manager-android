package app.threedollars.domain.repository

import app.threedollars.common.Resource
import app.threedollars.domain.dto.AppStatusDto

interface AppRepository {
    suspend fun getAppStatus(): Resource<AppStatusDto>
}
