package app.threedollars.manager

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.threedollars.manager.screen.AddStoreScreen
import app.threedollars.manager.screen.CategoryScreen
import app.threedollars.manager.screen.HomeScreen
import app.threedollars.manager.screen.MyPageScreen
import app.threedollars.manager.sign.ui.SignUpFormScreen
import app.threedollars.manager.sign.ui.SocialLoginScreen
import app.threedollars.manager.splash.SplashScreen

@Composable
fun NavController(kakaoLogin: () -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            BottomNavItem.SplashScreen.route,
            modifier = Modifier.fillMaxSize()
        )
        {
            composable(route = BottomNavItem.SplashScreen.route) {
                SplashScreen(navController)
            }
            composable(route = BottomNavItem.HomeScreen.route) {
                SplashScreen(navController)
            }
            composable(route = BottomNavItem.LoginScreen.route) {
                SocialLoginScreen(navController, kakaoLogin)
            }
            composable(route = BottomNavItem.SignUpFormScreen.route) {
                SignUpFormScreen()
            }
        }
    }
}

@Composable
fun HomeNavController(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Home.route) {
            HomeScreen()
        }
        composable(BottomNavItem.Category.route) {
            CategoryScreen()
        }
        composable(BottomNavItem.AddStore.route) {
            AddStoreScreen()
        }
        composable(BottomNavItem.MyPage.route) {
            MyPageScreen()
        }
    }
}

