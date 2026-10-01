package app.threedollars.manager.fake

import app.threedollars.common.Resource
import app.threedollars.domain.dto.AppStatusDto
import app.threedollars.domain.repository.AppRepository

class FakeAppRepository(
    vararg responses: Resource<AppStatusDto>,
) : AppRepository {

    private val queue = ArrayDeque(responses.toList())

    var callCount: Int = 0
        private set

    override suspend fun getAppStatus(): Resource<AppStatusDto> {
        callCount++
        return if (queue.size > 1) queue.removeFirst() else queue.first()
    }
}
