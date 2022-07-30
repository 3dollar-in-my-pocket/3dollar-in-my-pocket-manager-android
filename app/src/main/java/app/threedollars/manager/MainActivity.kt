package app.threedollars.manager

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.threedollars.data.oauth.KakaoLogin
import app.threedollars.domain.LoginMethod
import app.threedollars.domain.entity.SocialLoginToken
import app.threedollars.manager.screen.*
import app.threedollars.manager.sign.ui.component.LoginButtons
import app.threedollars.manager.sign.viewmodel.LoginViewModel
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var kakaoLogin: KakaoLogin

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NavController {
                startKakaoLogin {
                    loginViewModel.startKakaoLogin(it)
                }
            }
        }
    }

    private fun startKakaoLogin(
        onResult: (Result<SocialLoginToken>) -> Unit
    ) {
        val loginResCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            Log.d("login", token.toString() + " " + error.toString())
            if (error != null) {
                onResult(Result.failure(error))
            } else if (token != null) {
                onResult(
                    Result.success(
                        SocialLoginToken(
                            token.accessToken,
                            token.refreshToken,
                            LoginMethod.KAKAO
                        )
                    )
                )
            }
        }

        if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
            LoginClient.instance.loginWithKakaoTalk(
                context = this,
                callback = loginResCallback
            )
        } else {
            LoginClient.instance.loginWithKakaoAccount(
                context = this,
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


