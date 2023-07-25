package app.threedollars.manager.sign.ui

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import app.threedollars.manager.MainActivity
import app.threedollars.manager.R
import app.threedollars.manager.sign.LoginNavItem
import app.threedollars.manager.sign.viewmodel.SplashViewModel
import app.threedollars.manager.util.findActivity
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, viewModel: SplashViewModel = hiltViewModel()) {
    val navItem by viewModel.loginNavItem.collectAsStateWithLifecycle(null)
    val context = LocalContext.current

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.partners_splash))
    val lottieAnimatable = rememberLottieAnimatable()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center,
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

    LaunchedEffect(Unit) {
        delay(2000L)
        navItem?.let {
            if (it == LoginNavItem.Home) {
                context.startActivity(Intent(context, MainActivity::class.java))
                context.findActivity().finish()
            } else {
                navController.popBackStack()
                navController.navigate(it.screenRoute)
            }
        }
    }
}