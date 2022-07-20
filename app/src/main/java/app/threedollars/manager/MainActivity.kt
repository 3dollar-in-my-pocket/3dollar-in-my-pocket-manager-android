package app.threedollars.manager

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.threedollars.manager.screen.*
import app.threedollars.manager.sign.ui.component.LoginButtons
import app.threedollars.manager.splash.SplashScreen
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashScreen()
        }
    }

    private fun tryKakaoLogin() {
        val loginResCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("localClassName", "로그인 실패", error)
                // showToast(R.string.error_no_kakao_login)
            } else if (token != null) {
                Log.d("localClassName", token.toString())
                // tryLoginBySocialType(token)
            }
        }
    }

    private fun tryKakaoLogin() {
        val loginResCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("localClassName", "로그인 실패", error)
                // showToast(R.string.error_no_kakao_login)
            } else if (token != null) {
                Log.d("localClassName", token.toString())
                // tryLoginBySocialType(token)
            }
        }

        if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
            LoginClient.instance.loginWithKakaoTalk(this, callback = loginResCallback)
        } else {
            LoginClient.instance.loginWithKakaoAccount(
                this,
                callback = loginResCallback
            )
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


