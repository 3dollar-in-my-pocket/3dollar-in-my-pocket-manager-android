package app.threedollars.manager.storeManagement.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.threedollars.common.ui.*
import app.threedollars.manager.storeManagement.viewModel.MyViewModel
import coil.compose.AsyncImage

@Composable
fun MyScreen(viewModel: MyViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}

@Preview
@Composable
fun displayProfileInfo() {
    Box(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(model ="https://developer.android.com/static/images/jetpack/compose/layout-jetnews-scaffold.png?hl=ko", contentDescription = "대표 사진", modifier = Modifier.fillMaxWidth().height(240.dp))
        ProfileContents()
    }
}


@Composable
fun ProfileContents() {
    val list = listOf("게임", "독서", "운동")
    val snsUrl = "instagram.com/3dollar_in_my_pocket?utm_medium=copy_link"
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 210.dp)
            .shadow(8.dp)
            .background(White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp, 20.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "ssssssssss", fontSize = 24.sp,
            fontWeight = FontWeight.Bold, color = Gray100, style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )
        LazyRow(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(list) {
                CategoryItem(it)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray0, shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Text(text = "SNS", modifier = Modifier.width(44.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(41.dp))
            Text(text = snsUrl, color = Gray50, fontSize = 12.sp, textAlign = TextAlign.End)
        }
    }
}

@Composable
fun CategoryItem(category: String) {
    Text(
        text = category, modifier = Modifier
            .background(color = Green_OP10, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp), color = Green, fontSize = 14.sp, style = TextStyle(
            platformStyle = PlatformTextStyle(includeFontPadding = false)
        )
    )
}