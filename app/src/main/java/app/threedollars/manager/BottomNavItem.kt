package app.threedollars.manager

sealed class BottomNavItem(var icon : Int, var screenRoute : String) {
    object Home : BottomNavItem(R.drawable.ic_home,"home")
    object Category: BottomNavItem(R.drawable.ic_category,"category")
    object AddStore: BottomNavItem(R.drawable.ic_write,"addStore")
    object MyPage: BottomNavItem(R.drawable.ic_my,"myPage")
}