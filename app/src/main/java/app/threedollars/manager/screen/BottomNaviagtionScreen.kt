package app.threedollars.manager.screen

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Gray50
import app.threedollars.manager.BottomNavItem
import app.threedollars.manager.R

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.StoreManagement,
        BottomNavItem.Setting
    )
    var backgroundColor by remember { mutableStateOf(Color.White) }

    androidx.compose.material.BottomNavigation(
        backgroundColor = backgroundColor,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = null) },
                selectedContentColor = colorResource(id = R.color.green500),
                unselectedContentColor = Gray50,
                alwaysShowLabel = true,
                selected = currentRoute == item.screenRoute,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        backgroundColor = if (item.screenRoute == "setting") {
                            Gray100
                        } else {
                            Color.White
                        }
                        navController.graph.startDestinationRoute?.let { screenRoute ->
                            popUpTo(screenRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}