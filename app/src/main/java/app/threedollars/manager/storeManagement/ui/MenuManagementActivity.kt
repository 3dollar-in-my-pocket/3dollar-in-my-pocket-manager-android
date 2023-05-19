package app.threedollars.manager.storeManagement.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import app.threedollars.common.ext.getResourceUri
import app.threedollars.common.ext.toast
import app.threedollars.common.ui.Gray0
import app.threedollars.common.ui.Gray30
import app.threedollars.common.ui.Gray5
import app.threedollars.common.ui.Gray70
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.Red
import app.threedollars.manager.R
import app.threedollars.manager.storeManagement.viewModel.MyViewModel
import app.threedollars.manager.util.ContentUriToRequestBody
import app.threedollars.manager.vo.MenusVo
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.RequestBody
import java.text.DecimalFormat

@AndroidEntryPoint
class MenuManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MenuManagementScreen()
        }
    }

    @Composable
    fun MenuManagementScreen(viewModel: MyViewModel = hiltViewModel()) {
        val bossStore = viewModel.bossStoreRetrieveMe.collectAsState(null)
        val isSuccess = viewModel.isSuccess.collectAsState(null)
        val context = LocalContext.current
        var menuList by remember { mutableStateOf(bossStore.value?.menus?.toMutableList() ?: mutableListOf()) }

        LaunchedEffect(isSuccess.value) {
            if (isSuccess.value == true) {
                setResult(RESULT_OK)
                finish()
                context.toast("수정 완료")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Gray0)
        ) {
            var isClickDeleteButton by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp)
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {
                            finish()
                        }, painter = painterResource(id = R.drawable.ic_back), contentDescription = ""
                )
                Text(
                    modifier = Modifier.align(Alignment.Center), text = "메뉴 관리", fontSize = 16.sp, color = Color.Black
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 60.dp, bottom = 16.dp)
            ) {
                Text(buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Gray70)) {
                        append("${bossStore.value?.menus?.size}/20개")
                    }
                    append("의 메뉴가 등록되어 있습니다.")
                }, modifier = Modifier.align(Alignment.CenterStart), color = Gray30, fontWeight = FontWeight.W500, fontSize = 14.sp)
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable {
                            if (isClickDeleteButton) {
                                // TODO: 전체 삭제 기능
                            } else isClickDeleteButton = true
                        },
                    text = if (isClickDeleteButton) "전체 삭제" else "삭제",
                    fontSize = 14.sp,
                    color = Red,
                    fontWeight = FontWeight.W700
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                if (menuList.isEmpty()) {
                    menuList.add(MenusVo())
                }
                items(count = menuList.size) { index ->
                    var menuName by remember { mutableStateOf(TextFieldValue("")) }
                    var price by remember { mutableStateOf(TextFieldValue("")) }

                    Box(
                        modifier = Modifier
                            .width(327.dp)
                            .height(164.dp)
                            .padding(top = 16.dp, bottom = 8.dp)
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        MenuPhoto(Modifier.align(Alignment.CenterStart)) {}
                        TextField(
                            value = menuName,
                            onValueChange = { newText ->
                                if (newText.text.length <= 10) {
                                    menuName = newText
                                    menuList[index].name = newText.text.ifEmpty { null }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .padding(start = 12.dp)
                                .align(Alignment.TopEnd),
                            placeholder = { Text("메뉴를 입력해 주세요", fontSize = 14.sp, fontWeight = FontWeight.W400) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                placeholderColor = Gray30,
                                backgroundColor = Gray5,
                                cursorColor = Gray30,
                                disabledTextColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.W400)
                        )
                        TextField(
                            value = price,
                            onValueChange = { newText ->
                                if (newText.text.length <= 10) {
                                    val newPrice: String = newText.text.replace(",", "").replace("원", "")
                                    val formatter = DecimalFormat("###,###")
                                    val result =
                                        if (newPrice.isEmpty()) {
                                            newPrice
                                        } else {
                                            "${formatter.format(newPrice.toInt())}원"
                                        }
                                    price = newText.copy(result)
                                    menuList[index].price = if (newPrice.isEmpty()) null else newPrice.toInt()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.65f)
                                .padding(start = 12.dp, top = 8.dp)
                                .align(Alignment.BottomEnd),
                            placeholder = { Text("가격을 입력해 주세요", fontSize = 14.sp, fontWeight = FontWeight.W400) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                            shape = RoundedCornerShape(8.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                placeholderColor = Gray30,
                                backgroundColor = Gray5,
                                cursorColor = Gray30,
                                disabledTextColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.W400)
                        )

                        Text(text = "* 메뉴명, 가격, 사진을 모두 등록해주세요.", fontSize = 12.sp, fontWeight = FontWeight.W400, color = Red)
                    }
                }
                item {
                    if (menuList.size < 20) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp)
                                .border(1.dp, SolidColor(Green), RoundedCornerShape(8.dp))
                                .clickable {
                                    menuList = (menuList + mutableListOf(MenusVo())) as MutableList<MenusVo>
                                },
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                painter = painterResource(id = R.drawable.ic_plus_circle),
                                contentDescription = ""
                            )
                            Text(
                                modifier = Modifier.padding(start = 8.dp, top = 14.dp, bottom = 14.dp),
                                text = "메뉴 추가하기",
                                fontWeight = FontWeight.W700,
                                fontSize = 14.sp,
                                color = Green
                            )
                        }
                    }

                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Green, contentColor = Color.White),
                        onClick = {
                        }) {
                        Text(text = "저장하기", fontSize = 16.sp, fontWeight = FontWeight.W500)
                    }
                }
            }
        }
    }

    @Composable
    fun MenuPhoto(modifier: Modifier, onChangeUri: (RequestBody) -> Unit) {
        val context = LocalContext.current
        var selectImage by remember {
            mutableStateOf(R.drawable.ic_menu_default.getResourceUri(context))
        }
        val galleryLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    selectImage = uri
                    onChangeUri(ContentUriToRequestBody(context, uri))
                }
            }
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(selectImage)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_menu_default),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .width(104.dp)
                .height(104.dp)
                .clip(shape = RoundedCornerShape(16.dp))
                .clickable {
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
        )
    }
}