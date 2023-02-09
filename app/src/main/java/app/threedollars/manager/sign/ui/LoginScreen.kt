package app.threedollars.manager.sign.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import app.threedollars.common.ui.Yellow
import app.threedollars.manager.R
import app.threedollars.manager.sign.LoginNavItem
import app.threedollars.manager.sign.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginKakao: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val isLogin by viewModel.isLogin.collectAsStateWithLifecycle(null)

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = R.drawable.ic_login_bg), contentDescription = null)
        SocialLoginButton {
            loginKakao()
        }
    }
    LaunchedEffect(isLogin) {
        isLogin?.let {
            if (it) {
                // TODO: 홈 화면
            } else {
                navController.popBackStack()
                navController.navigate(LoginNavItem.Sign.screenRoute)
            }
        }
    }
}

@Composable
fun SocialLoginButton(
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .fillMaxWidth(),
            onClick = { onClick() },
            shape = RoundedCornerShape(23.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.Black,
                backgroundColor = Yellow
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_kakao),
                    contentDescription = null,

                    )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(id = R.string.kakao_login),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}

