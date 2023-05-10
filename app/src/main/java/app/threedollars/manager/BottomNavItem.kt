package app.threedollars.manager

sealed class BottomNavItem(var icon : Int, var screenRoute : String) {
    object Home : BottomNavItem(R.drawable.ic_home,"home")
    object StoreManagement: BottomNavItem(R.drawable.ic_truck,"storeManagement")
    object MyPage: BottomNavItem(R.drawable.ic_my,"myPage")
}