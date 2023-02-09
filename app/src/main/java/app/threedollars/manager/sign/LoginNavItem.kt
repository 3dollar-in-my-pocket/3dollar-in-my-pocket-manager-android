package app.threedollars.manager.sign

sealed class LoginNavItem(var screenRoute: String) {
    object Login : LoginNavItem("login")
    object Sign : LoginNavItem("sign")
}