package app.threedollars.manager.sign.viewmodel

import app.threedollars.manager.sign.LoginNavItem

sealed interface SplashEffect {
    data object OnUnexpectedError : SplashEffect
    data class OnNavigate(val item: LoginNavItem) : SplashEffect
}
