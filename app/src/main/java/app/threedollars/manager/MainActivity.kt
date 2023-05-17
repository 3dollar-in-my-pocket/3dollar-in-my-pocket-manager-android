package app.threedollars.manager

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.threedollars.manager.screen.*
import androidx.compose.ui.tooling.preview.Preview
import app.threedollars.manager.main.ui.HomeScreen
import app.threedollars.manager.setting.FaqScreen
import app.threedollars.manager.setting.SettingNavItem
import app.threedollars.manager.setting.SettingScreen
import app.threedollars.manager.storeManagement.ui.StoreManagementScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreenView()
        }
    }

}

@Preview
@Composable
fun MainScreenView() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {
        NavigationGraph(navController = navController)
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Home.screenRoute) {
        composable(BottomNavItem.Home.screenRoute) {
            HomeScreen()
        }
        composable(BottomNavItem.StoreManagement.screenRoute) {
            StoreManagementScreen()
        }
        composable(BottomNavItem.Setting.screenRoute) {
            SettingScreen(navController)
        }
        composable(SettingNavItem.Faq.screenRoute) {
            FaqScreen(navController)
        }
    }
}
