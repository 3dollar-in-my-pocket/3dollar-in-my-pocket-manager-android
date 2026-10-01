package app.threedollars.manager.sign.viewmodel

import app.threedollars.common.Resource
import app.threedollars.domain.dto.AppStatusDto

const val MANAGER_APP_STORE_URL = "https://play.google.com/store/apps/details?id=app.threedollars.manager"

sealed interface AppStatusResult {
    data object Proceed : AppStatusResult
    data class ForceUpdate(val title: String?, val message: String?, val linkUrl: String) : AppStatusResult
    data object Maintenance : AppStatusResult
    data object Error : AppStatusResult
}

fun Resource<AppStatusDto>.toAppStatusResult(): AppStatusResult {
    val appStatus = data
    return when {
        this is Resource.Success && appStatus != null -> {
            val forceUpdate = appStatus.forceUpdate
            if (forceUpdate.enabled) {
                AppStatusResult.ForceUpdate(
                    title = forceUpdate.title?.takeIf { it.isNotBlank() },
                    message = forceUpdate.message?.takeIf { it.isNotBlank() },
                    linkUrl = forceUpdate.linkUrl?.takeIf { it.isNotBlank() } ?: MANAGER_APP_STORE_URL,
                )
            } else {
                AppStatusResult.Proceed
            }
        }

        code == "503" -> AppStatusResult.Maintenance
        else -> AppStatusResult.Error
    }
}
