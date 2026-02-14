package app.threedollars.manager

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.threedollars.common.MaintenanceStateManager
import app.threedollars.common.ui.MaintenanceDialog
import app.threedollars.manager.sign.LoginNavItem
import app.threedollars.manager.util.findActivity
import kotlin.system.exitProcess
import app.threedollars.manager.sign.ui.LoginScreen
import app.threedollars.manager.sign.ui.SignScreen
import app.threedollars.manager.sign.ui.SplashScreen
import app.threedollars.manager.sign.ui.WaitingScreen
import app.threedollars.manager.sign.viewmodel.LoginViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginNavigationGraph(loginViewModel) {
                loginKakao()
            }
        }
    }

    private fun loginKakao() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("kakao", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                loginViewModel.login(token.accessToken)
                Log.i("TAG", "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e("kakao", "카카오톡으로 로그인 실패", error)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    loginViewModel.login(token.accessToken)
                    Log.i("TAG", "카카오톡으로 로그인 성공 ${token.accessToken}")
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }
}

@Composable
fun LoginNavigationGraph(viewModel: LoginViewModel, loginKakao: () -> Unit) {
    val navController = rememberNavController()
    val showMaintenanceDialog by MaintenanceStateManager.showMaintenanceDialog.collectAsState()
    val context = LocalContext.current

    NavHost(
        navController, startDestination = LoginNavItem.Splash.screenRoute, modifier = Modifier.fillMaxSize()
    ) {
        composable(LoginNavItem.Splash.screenRoute) {
            SplashScreen(navController)
        }

        composable(LoginNavItem.Login.screenRoute) {
            LoginScreen(navController, loginKakao, viewModel)
        }
        composable(LoginNavItem.Sign.screenRoute) {
            SignScreen(navController)
        }
        composable(LoginNavItem.Waiting.screenRoute) {
            WaitingScreen(navController)
        }
    }

    // 503 에러 시 점검 다이얼로그 표시
    if (showMaintenanceDialog) {
        MaintenanceDialog(
            onDismiss = {
                // 재시도: 앱 종료 후 재시작 유도
                MaintenanceStateManager.reset()
                context.findActivity().finish()
                exitProcess(0)
            }
        )
    }
}