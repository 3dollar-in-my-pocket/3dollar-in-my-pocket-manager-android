package app.threedollars.manager

sealed class BottomNavItem(var icon: Int, var screenRoute: String) {
    object Home : BottomNavItem(R.drawable.ic_home, "home")
    object Management : BottomNavItem(R.drawable.ic_management, "management")
    object MyPage : BottomNavItem(R.drawable.ic_my, "myPage")
}