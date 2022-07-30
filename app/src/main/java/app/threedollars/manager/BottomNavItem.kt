package app.threedollars.manager

import androidx.annotation.StringRes

sealed class BottomNavItem(
    val route: String,
    @StringRes val resourceId: Int
) {
    object HomeScreen : BottomNavItem("HomeScreen", R.string.app_name)
    object SplashScreen : BottomNavItem("SplashScreen", R.string.app_name)
    object LoginScreen : BottomNavItem("LoginScreen", R.string.app_name)
    object SignUpFormScreen : BottomNavItem("SignUpFormScreen", R.string.app_name)
}