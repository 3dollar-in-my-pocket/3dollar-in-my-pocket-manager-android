package app.threedollars.manager

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.threedollars.manager.home.screen.HomeScreen
import app.threedollars.manager.screen.*
import app.threedollars.manager.setting.MyPageScreen
import app.threedollars.manager.sign.ui.LoginButtons
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreenView()
//            SocialLoginScreen()
        }
    }
}

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
fun SocialLoginScreen() {
    Column {
        LoginButtons()
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Home.screenRoute) {
        composable(BottomNavItem.Home.screenRoute) {
            HomeScreen()
        }
        composable(BottomNavItem.Management.screenRoute) {
            ManagementScreen()
        }
        composable(BottomNavItem.MyPage.screenRoute) {
            MyPageScreen()
        }
    }
}


