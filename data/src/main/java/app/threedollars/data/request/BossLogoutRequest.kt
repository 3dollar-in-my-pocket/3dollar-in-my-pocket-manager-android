package app.threedollars.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BossLogoutRequest(
    @SerialName("logoutDevice")
    val logoutDevice: LogoutDeviceRequest? = null,
) {
    @Serializable
    data class LogoutDeviceRequest(
        @SerialName("pushPlatform")
        val pushPlatform: String,
        @SerialName("pushToken")
        val pushToken: String,
    )

    companion object {
        private const val PUSH_PLATFORM_FCM = "FCM"

        fun of(pushToken: String?) = BossLogoutRequest(
            logoutDevice = pushToken
                ?.takeIf { it.isNotBlank() }
                ?.let { LogoutDeviceRequest(pushPlatform = PUSH_PLATFORM_FCM, pushToken = it) }
        )
    }
}
