package app.threedollars.manager.sign.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import app.threedollars.common.BaseDialog
import app.threedollars.common.BaseDialogStyle
import app.threedollars.common.ui.effect.FlowWithLifecycleEffect
import app.threedollars.manager.MainActivity
import app.threedollars.manager.R
import app.threedollars.manager.sign.LoginNavItem
import app.threedollars.manager.sign.viewmodel.AppStatusResult
import app.threedollars.manager.sign.viewmodel.SplashEffect
import app.threedollars.manager.sign.viewmodel.SplashViewModel
import app.threedollars.manager.util.findActivity
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun SplashScreen(navController: NavController, viewModel: SplashViewModel = hiltViewModel()) {
    val context = LocalContext.current

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.partners_splash))
    val lottieAnimatable = rememberLottieAnimatable()
    val isLottieCompleted by remember {
        derivedStateOf {
            lottieAnimatable.progress >= 1f
        }
    }

    var showRetry by remember {
        mutableStateOf(false)
    }
    var navItem by remember {
        mutableStateOf<LoginNavItem?>(null)
    }
    var forceUpdate by remember {
        mutableStateOf<AppStatusResult.ForceUpdate?>(null)
    }

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

    FlowWithLifecycleEffect(
        flow = viewModel.effect
    ) {
        when (it) {
            is SplashEffect.OnUnexpectedError -> {
                showRetry = true
            }

            is SplashEffect.OnNavigate -> {
                navItem = it.item
            }

            is SplashEffect.OnForceUpdate -> {
                forceUpdate = it.forceUpdate
            }
        }
    }

    LaunchedEffect(composition) {
        lottieAnimatable.animate(
            composition = composition,
            clipSpec = LottieClipSpec.Frame(0, 1200),
            initialProgress = 0f
        )
    }

    LaunchedEffect(navItem, isLottieCompleted) {
        if (!isLottieCompleted) {
            return@LaunchedEffect
        }
        navItem?.let {
            when (it) {
                LoginNavItem.Home -> {
                    context.startActivity(Intent(context, MainActivity::class.java))
                    context.findActivity().finish()
                }

                else -> {
                    navController.popBackStack()
                    navController.navigate(it.screenRoute)
                }
            }
        }
    }

    forceUpdate?.takeIf { isLottieCompleted }?.let { update ->
        BaseDialog(
            title = update.title ?: stringResource(R.string.force_update_title),
            message = update.message ?: stringResource(R.string.force_update_message),
            confirmText = stringResource(R.string.force_update_confirm),
            style = BaseDialogStyle(
                titleFontSize = 18.sp,
                messageFontSize = 16.sp
            ),
            onConfirm = {
                context.openUrl(update.linkUrl)
            }
        )
    }

    if (showRetry) {
        BaseDialog(
            title = stringResource(R.string.error_unknown_title),
            message = stringResource(R.string.error_unknown_message),
            confirmText = stringResource(R.string.retry),
            style = BaseDialogStyle(
                titleFontSize = 18.sp,
                messageFontSize = 16.sp
            ),
            onConfirm = {
                showRetry = false
                viewModel.retry()
            }
        )
    }
}

private fun Context.openUrl(url: String) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    } catch (e: ActivityNotFoundException) {
        Unit
    }
}
