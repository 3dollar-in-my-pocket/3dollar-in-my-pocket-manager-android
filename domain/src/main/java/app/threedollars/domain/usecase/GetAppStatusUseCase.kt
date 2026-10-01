package app.threedollars.domain.usecase

import app.threedollars.common.Resource
import app.threedollars.domain.dto.AppStatusDto
import app.threedollars.domain.repository.AppRepository
import javax.inject.Inject

class GetAppStatusUseCase @Inject constructor(
    private val appRepository: AppRepository,
) {
    suspend operator fun invoke(): Resource<AppStatusDto> = appRepository.getAppStatus()
}
