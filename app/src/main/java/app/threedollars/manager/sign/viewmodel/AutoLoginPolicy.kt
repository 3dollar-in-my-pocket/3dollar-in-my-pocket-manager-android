package app.threedollars.manager.sign.viewmodel

sealed interface KakaoTokenResult {
    data class Success(val accessToken: String) : KakaoTokenResult
    data object Expired : KakaoTokenResult
    data object Failure : KakaoTokenResult

    companion object {
        fun of(accessToken: String?): KakaoTokenResult =
            accessToken?.takeIf { it.isNotBlank() }?.let(::Success) ?: Failure
    }
}

sealed interface AutoLoginDecision {
    data class Login(val accessToken: String) : AutoLoginDecision
    data object GoToLogin : AutoLoginDecision
    data object Error : AutoLoginDecision
}

object AutoLoginPolicy {

    fun isLoggedIn(storedSocialToken: String?): Boolean = !storedSocialToken.isNullOrBlank()

    fun decide(refreshResult: KakaoTokenResult): AutoLoginDecision = when (refreshResult) {
        is KakaoTokenResult.Success -> AutoLoginDecision.Login(refreshResult.accessToken)
        KakaoTokenResult.Expired -> AutoLoginDecision.GoToLogin
        KakaoTokenResult.Failure -> AutoLoginDecision.Error
    }
}
