package app.threedollars.manager.storeManagement.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.threedollars.common.ui.Gray0
import app.threedollars.common.ui.Green
import app.threedollars.manager.R
import app.threedollars.manager.storeManagement.viewModel.ProfileEditViewModel
import app.threedollars.manager.util.findActivity

@Composable
fun ProfileEditScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(Gray0)
    ) {
        ProfileEditTop()
        ProfileEditBottom()
    }
}

@Preview
@Composable
fun ProfileEditTop() {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(modifier = Modifier
            .wrapContentWidth()
            .padding(start = 16.dp), onClick = {
            context.findActivity().finish()
        }) {
            Image(painter = painterResource(id = R.drawable.ic_back), contentDescription = "")
        }
        Text(
            modifier = Modifier.fillMaxWidth(0.8f),
            text = "대표 정보 수정",
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
        )
    }
}

@Preview
@Composable
fun ProfileEditContents(viewModel: ProfileEditViewModel = hiltViewModel()) {
    val scrollState = rememberScrollState()
    var storeName by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .verticalScroll(scrollState)
    ) {

    }
}

@Preview
@Composable
fun ProfileEditBottom(onClick: () -> Unit = {}) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 21.5.dp),
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            backgroundColor = Green
        )
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = " 저장하기",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
