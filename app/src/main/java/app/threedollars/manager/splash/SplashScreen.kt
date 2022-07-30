package app.threedollars.manager.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import app.threedollars.manager.BottomNavItem
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {

    val viewModel: SplashViewModel = hiltViewModel()

    val nextScreen = viewModel.nextScreen.collectAsState(initial = null)

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "스플래시",
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }

    LaunchedEffect(nextScreen) {
        delay(500L) // 추후 애니메이션으로 교체
        if (nextScreen.value != null) {
            val route = (nextScreen.value as BottomNavItem).route
            navController.popBackStack()
            navController.navigate(route)
        }
    }
}