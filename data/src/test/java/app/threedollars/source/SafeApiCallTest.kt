package app.threedollars.source

import app.threedollars.common.Resource
import app.threedollars.data.BaseResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class SafeApiCallTest {

    // 회귀: 오프라인(UnknownHostException 등 IOException)에서 앱이 크래시하지 않고 Resource.Error 로 떨어진다
    @Test
    fun `회귀_네트워크_예외는_Resource_Error로_변환된다`() {
        // Given
        val call: suspend () -> Response<BaseResponse<String>> = { throw IOException("Unable to resolve host") }
        // When
        val result = runBlocking { safeApiCall(call) }
        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Unable to resolve host", result.errorMessage)
    }

    @Test
    fun `회귀_성공_응답은_Resource_Success로_변환된다`() {
        // Given
        val call: suspend () -> Response<BaseResponse<String>> = { Response.success(BaseResponse(data = "OK")) }
        // When
        val result = runBlocking { safeApiCall(call) }
        // Then
        assertTrue(result is Resource.Success)
        assertEquals("OK", result.data)
        assertEquals("200", result.code)
    }
}
