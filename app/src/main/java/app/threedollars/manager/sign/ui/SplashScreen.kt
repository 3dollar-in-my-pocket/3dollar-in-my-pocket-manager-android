package app.threedollars.manager.sign.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import app.threedollars.manager.R
import app.threedollars.manager.sign.LoginNavItem
import app.threedollars.manager.sign.viewmodel.LoginViewModel
import app.threedollars.manager.sign.viewmodel.SplashViewModel
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, viewModel: SplashViewModel = hiltViewModel()) {
    val isLogin by viewModel.isLogin.collectAsStateWithLifecycle(null)


    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.partners_splash))
    val lottieAnimatable = rememberLottieAnimatable()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LottieAnimation(
            composition = composition,
            contentScale = ContentScale.FillWidth
        )
    }

    LaunchedEffect(composition) {
        lottieAnimatable.animate(
            composition = composition,
            clipSpec = LottieClipSpec.Frame(0, 1200),
            initialProgress = 0f
        )
    }

    LaunchedEffect(isLogin) {
        delay(2000L)
        isLogin?.let {
            if (it) {
                // TODO: 홈 화면
            } else {
                navController.popBackStack()
                navController.navigate(LoginNavItem.Login.screenRoute)
            }
        }
    }
}