package app.threedollars.network

import app.threedollars.common.MaintenanceStateManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class MaintenanceInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

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