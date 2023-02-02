package app.threedollars.manager

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.*
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.threedollars.manager.screen.*
import androidx.compose.ui.tooling.preview.Preview
import app.threedollars.manager.sign.ui.LoginButtons
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            MainScreenView()
//            SocialLoginScreen()
        }
    }
}

@Preview
@Composable
fun MainScreenView() {
    val navController = rememberNavController()
//    Scaffold(
//        bottomBar = { BottomNavigation(navController = navController) }
//    ) {
//        NavigationGraph(navController = navController)
//    }
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
        composable(BottomNavItem.Category.screenRoute) {
            CategoryScreen()
        }
        composable(BottomNavItem.AddStore.screenRoute) {
            AddStoreScreen()
        }
        composable(BottomNavItem.MyPage.screenRoute) {
            MyPageScreen()
        }
    }
}


