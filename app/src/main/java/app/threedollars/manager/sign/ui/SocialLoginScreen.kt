package app.threedollars.manager.sign.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import app.threedollars.manager.BottomNavItem
import app.threedollars.manager.R
import app.threedollars.manager.sign.ui.component.SocialLoginButton
import app.threedollars.manager.sign.viewmodel.LoginViewModel

@Composable
fun SocialLoginScreen(
    navController: NavHostController,
    kakaoLogin: () -> Unit
) {

    val viewModel: LoginViewModel = hiltViewModel()

    val nextScreen = viewModel.nextScreen.collectAsState(initial = null)

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_back),
            contentDescription = "",
            modifier = Modifier
                .padding(top = 150.dp, bottom = 24.dp),
            contentScale = ContentScale.Crop
        )
        SocialLoginButton {
            kakaoLogin()
        }
    }

    LaunchedEffect(nextScreen) {
        if (nextScreen.value != null) {
            val route = (nextScreen.value as BottomNavItem).route
            navController.popBackStack()
            navController.navigate(route)
        }
    }
}