package app.threedollars.manager.sign.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AutoLoginPolicyTest {

    // TH-1513 TC1
    @Test
    fun `TH1513_TC1_카카오_토큰을_새로_받으면_그_토큰으로_자동로그인한다`() {
        // Given
        val refreshResult = KakaoTokenResult.Success("fresh-token")
        // When
        val decision = AutoLoginPolicy.decide(refreshResult)
        // Then
        assertEquals(AutoLoginDecision.Login("fresh-token"), decision)
    }

    // TH-1513 TC2
    @Test
    fun `TH1513_TC2_리프레시_토큰까지_만료되면_로그인_화면으로_보낸다`() {
        // Given
        val refreshResult = KakaoTokenResult.Expired
        // When
        val decision = AutoLoginPolicy.decide(refreshResult)
        // Then
        assertEquals(AutoLoginDecision.GoToLogin, decision)
    }

    // TH-1513 TC3
    @Test
    fun `TH1513_TC3_네트워크_오류로_갱신에_실패하면_로그아웃하지_않고_재시도_오류를_띄운다`() {
        // Given
        val refreshResult = KakaoTokenResult.Failure
        // When
        val decision = AutoLoginPolicy.decide(refreshResult)
        // Then
        assertEquals(AutoLoginDecision.Error, decision)
    }

    // TH-1513 TC4
    @Test
    fun `TH1513_TC4_로그아웃해서_저장된_토큰이_없으면_자동로그인하지_않는다`() {
        // Given
        val loggedOut = listOf(null, "", " ")
        // When
        val results = loggedOut.map { AutoLoginPolicy.isLoggedIn(it) }
        // Then
        results.forEach { assertFalse(it) }
        assertTrue(AutoLoginPolicy.isLoggedIn("stored-token"))
    }

    // TH-1513 TC3
    @Test
    fun `TH1513_TC3_SDK가_빈_토큰을_주면_실패로_취급한다`() {
        // Given
        val accessTokens = listOf(null, "", " ")
        // When
        val results = accessTokens.map { KakaoTokenResult.of(it) }
        // Then
        results.forEach { assertEquals(KakaoTokenResult.Failure, it) }
    }
}
