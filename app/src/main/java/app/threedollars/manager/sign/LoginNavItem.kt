package app.threedollars.manager.sign

sealed class LoginNavItem(var screenRoute: String) {
    object Splash : LoginNavItem("splash")
    object Login : LoginNavItem("login")
    object Sign : LoginNavItem("sign")
}