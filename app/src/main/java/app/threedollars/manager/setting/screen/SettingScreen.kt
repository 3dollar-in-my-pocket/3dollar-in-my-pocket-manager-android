package app.threedollars.manager.setting.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import app.threedollars.data.store.response.MyAccountResponse
import app.threedollars.manager.R
import app.threedollars.manager.SettingNavItem
import app.threedollars.manager.setting.content.SettingCategoryContent
import app.threedollars.manager.setting.SettingViewModel

@Composable
fun SettingScreen(
    navController: NavHostController,
    viewModel: SettingViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.black))
    ) {

        val myAccountResponse: MyAccountResponse by viewModel.myAccountResponse.observeAsState(
            MyAccountResponse()
        )

        Text(
            text = "설정",
            fontWeight = FontWeight.W600,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp),
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )

        Text(
            text = "${myAccountResponse.name} 사장님",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .padding(start = 24.dp, top = 36.dp),
            fontSize = 24.sp
        )
        Text(
            text = "오늘도 적게 일하고 많이 버세요!",
            fontWeight = FontWeight.Normal,
            color = Color.White,
            modifier = Modifier
                .padding(start = 24.dp, top = 8.dp),
            fontSize = 24.sp
        )
        SettingCategoryContent(
            leftText = "사업자 번호",
            rightText = myAccountResponse.businessNumber,
            rightTextColor = R.color.gray30,
            modifier = Modifier.padding(top = 20.dp)
        )
        SettingCategoryContent(
            leftText = "가슴속 삼천원팀에 연락하기", rightImage = R.drawable.ic_right_arrow,
            modifier = Modifier.padding(top = 8.dp),
            onClick = {
                // TODO: 연락 기능 구현
            }
        )
        SettingCategoryContent(
            leftText = "FAQ", rightImage = R.drawable.ic_right_arrow,
            modifier = Modifier.padding(top = 8.dp),
            onClick = {
                navController.navigate(SettingNavItem.Faq.screenRoute)
            }
        )
        SettingCategoryContent(
            leftText = "개인정보 처리방침", rightImage = R.drawable.ic_right_arrow,
            modifier = Modifier.padding(top = 8.dp),
            onClick = {
                navController.navigate(SettingNavItem.PrivacyPolicy.screenRoute)
            }
        )
        SettingCategoryContent(
            leftImage = if (myAccountResponse.socialType == "KAKAO") R.drawable.ic_logo_kakao else R.drawable.ic_logo_naver,
            leftText = if (myAccountResponse.socialType == "KAKAO") "카카오 계정 회원" else "네이버 계정 회원",
            rightText = "로그아웃",
            rightTextColor = R.color.red500,
            modifier = Modifier.padding(top = 8.dp),
            onClick = {
                // TODO: 로그아웃 구현
            }
        )
        Row(
            Modifier
                .padding(start = 24.dp, top = 24.dp)
                .clickable {
                    // TODO: 회원탈퇴 구현
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_alert_circle),
                contentDescription = ""
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "회원탈퇴",
                color = colorResource(id = R.color.gray40)
            )
        }
    }
}