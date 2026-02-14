package app.threedollars.network

import app.threedollars.common.MaintenanceStateManager
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

class MaintenanceInterceptor : Interceptor {

    companion object {
        // 테스트용 플래그: true로 설정하면 모든 API에 대해 503 응답 반환
        var FORCE_503_FOR_TESTING = false
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // 테스트 모드: 모든 요청에 대해 503 반환
        if (FORCE_503_FOR_TESTING) {
            MaintenanceStateManager.setMaintenanceMode(true)
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(503)
                .message("Service Unavailable - Testing Mode")
                .body("{}".toResponseBody(null))
                .build()
        }

        return try {
            val response = chain.proceed(request)

            when (response.code) {
                503 -> {
                    // 서버 점검 상태 설정
                    MaintenanceStateManager.setMaintenanceMode(true)
                    response
                }
                in 200..299 -> {
                    // 정상 응답 시 점검 모드 해제
                    MaintenanceStateManager.setMaintenanceMode(false)
                    response
                }
                else -> {
                    response
                }
            }
        } catch (e: IOException) {
            // 네트워크 오류는 점검 모드로 처리하지 않음
            throw e
        }
    }
}