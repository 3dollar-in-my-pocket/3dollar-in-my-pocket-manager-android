package app.threedollars.manager.sign.viewmodel

import app.threedollars.common.Resource
import app.threedollars.domain.dto.AppForceUpdateDto
import app.threedollars.domain.dto.AppStatusDto
import app.threedollars.domain.usecase.GetAppStatusUseCase
import app.threedollars.manager.fake.FakeAppRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class AppStatusResultTest {

    private fun success(forceUpdate: AppForceUpdateDto): Resource<AppStatusDto> =
        Resource.Success(data = AppStatusDto(forceUpdate = forceUpdate), code = "200")

    // TH-897 TC1
    @Test
    fun `TH897_TC1_enabled_true면_서버_문구로_강제업데이트_결과가_된다`() {
        // Given
        val resource = success(
            AppForceUpdateDto(enabled = true, title = "제목", message = "본문", linkUrl = "https://example.com/update")
        )
        // When
        val result = resource.toAppStatusResult()
        // Then
        assertEquals(AppStatusResult.ForceUpdate(title = "제목", message = "본문", linkUrl = "https://example.com/update"), result)
    }

    // TH-897 TC2
    @Test
    fun `TH897_TC2_title_message가_없거나_비어있으면_null이라_기본_문구를_쓴다`() {
        // Given
        val missing = success(AppForceUpdateDto(enabled = true, title = null, message = null))
        val blank = success(AppForceUpdateDto(enabled = true, title = " ", message = ""))
        // When
        val missingResult = missing.toAppStatusResult() as AppStatusResult.ForceUpdate
        val blankResult = blank.toAppStatusResult() as AppStatusResult.ForceUpdate
        // Then
        assertEquals(null, missingResult.title)
        assertEquals(null, missingResult.message)
        assertEquals(null, blankResult.title)
        assertEquals(null, blankResult.message)
    }

    // TH-897 TC3
    @Test
    fun `TH897_TC3_linkUrl이_있으면_그_링크를_연다`() {
        // Given
        val resource = success(AppForceUpdateDto(enabled = true, linkUrl = "https://example.com/update"))
        // When
        val result = resource.toAppStatusResult() as AppStatusResult.ForceUpdate
        // Then
        assertEquals("https://example.com/update", result.linkUrl)
    }

    // TH-897 TC4
    @Test
    fun `TH897_TC4_linkUrl이_없으면_사장님앱_스토어_페이지를_연다`() {
        // Given
        val missing = success(AppForceUpdateDto(enabled = true, linkUrl = null))
        val blank = success(AppForceUpdateDto(enabled = true, linkUrl = ""))
        // When
        val missingResult = missing.toAppStatusResult() as AppStatusResult.ForceUpdate
        val blankResult = blank.toAppStatusResult() as AppStatusResult.ForceUpdate
        // Then
        assertEquals("https://play.google.com/store/apps/details?id=app.threedollars.manager", missingResult.linkUrl)
        assertEquals(MANAGER_APP_STORE_URL, blankResult.linkUrl)
    }

    // TH-897 TC6
    @Test
    fun `TH897_TC6_enabled_false면_기존_흐름으로_진행한다`() {
        // Given
        val resource = success(AppForceUpdateDto(enabled = false, title = "제목", message = "본문"))
        // When
        val result = resource.toAppStatusResult()
        // Then
        assertEquals(AppStatusResult.Proceed, result)
    }

    // TH-897 TC8
    @Test
    fun `TH897_TC8_앱상태_API가_실패하면_오류_결과가_된다`() {
        // Given
        val serverError = Resource.Error<AppStatusDto>(errorMessage = "error", code = "500")
        val networkError = Resource.Error<AppStatusDto>(errorMessage = "network", code = null)
        // When
        val serverResult = serverError.toAppStatusResult()
        val networkResult = networkError.toAppStatusResult()
        // Then
        assertEquals(AppStatusResult.Error, serverResult)
        assertEquals(AppStatusResult.Error, networkResult)
    }

    // TH-897 TC8
    @Test
    fun `TH897_TC8_실패_후_재시도하면_앱상태를_다시_조회해_진행한다`() = runBlocking {
        // Given
        val repository = FakeAppRepository(
            Resource.Error(errorMessage = "error", code = "500"),
            success(AppForceUpdateDto(enabled = false)),
        )
        val getAppStatus = GetAppStatusUseCase(repository)
        // When
        val first = getAppStatus().toAppStatusResult()
        val retried = getAppStatus().toAppStatusResult()
        // Then
        assertEquals(AppStatusResult.Error, first)
        assertEquals(AppStatusResult.Proceed, retried)
        assertEquals(2, repository.callCount)
    }

    // TH-897 TC10
    @Test
    fun `TH897_TC10_서버점검_503이면_오류팝업_없이_점검화면에_맡긴다`() {
        // Given
        val resource = Resource.Error<AppStatusDto>(errorMessage = "maintenance", code = "503")
        // When
        val result = resource.toAppStatusResult()
        // Then
        assertEquals(AppStatusResult.Maintenance, result)
    }
}
