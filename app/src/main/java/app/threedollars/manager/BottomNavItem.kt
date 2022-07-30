package app.threedollars.manager

import androidx.annotation.StringRes

sealed class BottomNavItem(
    @StringRes val resourceId: Int,
    val route: String,
) {
    object HomeScreen : BottomNavItem(R.drawable.ic_home, "HomeScreen")
    object SplashScreen : BottomNavItem(R.drawable.ic_home, "SplashScreen")
    object LoginScreen : BottomNavItem(R.drawable.ic_home, "LoginScreen")
    object SignUpFormScreen : BottomNavItem(R.drawable.ic_home, "SignUpFormScreen")
    object Home : BottomNavItem(R.drawable.ic_home, "home")
    object Category : BottomNavItem(R.drawable.ic_category, "category")
    object AddStore : BottomNavItem(R.drawable.ic_write, "addStore")
    object MyPage : BottomNavItem(R.drawable.ic_my, "myPage")
}