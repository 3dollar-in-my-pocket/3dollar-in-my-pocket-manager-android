package app.threedollars.manager.main.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import app.threedollars.common.ext.toStringDefault
import app.threedollars.manager.R
import app.threedollars.manager.main.viewmodel.HomeViewModel
import app.threedollars.manager.util.getCurrentLocationName
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalNaverMapApi::class)
@Preview
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var isLocationPermission by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(), onResult = {
        isLocationPermission = it
    })
    when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) -> isLocationPermission = true
        else -> SideEffect { launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION) }
    }
    val bossStoreRetrieveMe by viewModel.bossStoreRetrieveMe.collectAsState(null)
    var location by remember { mutableStateOf(com.naver.maps.map.NaverMap.DEFAULT_CAMERA_POSITION.target) }
    var currentLocation by remember(location) { mutableStateOf(location) }
    var address by remember(location) { mutableStateOf(context.getCurrentLocationName(location)) }
    if (isLocationPermission) {
        rememberFusedLocationSource().activate {
            it?.let { location = LatLng(it) }
        }
    } else {
        address = "위치권한을 허락해주세요."
    }
    var isFoodTruckCheck by remember { mutableStateOf(false) }
    val isOpenServer by remember(bossStoreRetrieveMe) { mutableStateOf(bossStoreRetrieveMe?.openStatus?.status.toStringDefault() == "OPEN") }
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.77f)
        ) {
            MapView(modifier = Modifier, currentLocation)
            Text(
                text = address,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center, color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 44.dp)
                    .height(56.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .wrapContentHeight(Alignment.CenterVertically),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 8.dp, bottom = 32.dp)
                    .align(Alignment.BottomStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .clickable { isFoodTruckCheck = !isFoodTruckCheck }
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .padding(start = 12.dp, end = 11.dp, top = 10.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = if (isFoodTruckCheck) R.drawable.ic_check else R.drawable.ic_uncheck),
                        contentDescription = "체크박스"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "다른 푸드트럭 보기", fontSize = 14.sp, color = colorResource(id = R.color.gray100))
                }
                IconButton(onClick = { currentLocation = LatLng(location.latitude, location.longitude) }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = "내 위치 아이콘"
                    )
                }

            }
        }
        if (isOpenServer) {
            HomeBottomOn(Modifier.align(Alignment.BottomStart), bossStoreRetrieveMe?.openStatus?.openStartDateTime.toStringDefault()) {
                viewModel.storeClosed(bossStoreRetrieveMe?.bossStoreId.toStringDefault())
            }
        } else {
            HomeBottomOff(Modifier.align(Alignment.BottomStart)) {
                if (location.latitude != 0.0)
                    viewModel.storeOpen(bossStoreRetrieveMe?.bossStoreId.toStringDefault(), location)
            }
        }
    }
}

@Composable
fun HomeBottomOn(modifier: Modifier, openDate: String, click: () -> Unit) {
    var openTime by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while (true) {
            openTime = openDate.getTodayOpenTime()
            delay(1000L)
        }
    }
    Column(
        modifier
            .background(colorResource(id = R.color.green500), shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
            .fillMaxWidth()
            .fillMaxHeight(0.25f)
            .padding(start = 24.dp, end = 24.dp, top = 24.dp)
    ) {
        Row(modifier = Modifier.padding(start = 2.dp)) {
            Text(
                text = "오늘은",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = openTime,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White),
                modifier = Modifier
                    .background(colorResource(id = R.color.green300), shape = RoundedCornerShape(8.dp))
                    .wrapContentHeight(Alignment.CenterVertically)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }

        Text(
            text = "동안 영업중이시네요! 오늘도 대박나세요!",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White),
            modifier = Modifier.padding(start = 2.dp, top = 2.dp)
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "셔터 내리기",
            style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center, color = colorResource(id = R.color.green500)),
            modifier = Modifier
                .clickable { click() }
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .wrapContentHeight(Alignment.CenterVertically),
        )
    }
}

@Composable
fun HomeBottomOff(modifier: Modifier, click: () -> Unit) {
    Column(
        modifier
            .background(Color.White, shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp))
            .fillMaxWidth()
            .fillMaxHeight(0.25f)
            .padding(start = 24.dp, end = 24.dp, top = 24.dp)
    ) {
        Text(
            text = "지금 계신 위치에서 영업을 시작할까요?",
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(start = 2.dp)
        )
        Text(text = "영업을 시작하면 위치가 손님들에게 공개됩니다.", style = TextStyle(fontSize = 14.sp), modifier = Modifier.padding(start = 2.dp, top = 2.dp))
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "오늘의 영업 시작하기",
            style = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center, color = Color.White),
            modifier = Modifier
                .clickable { click() }
                .fillMaxWidth()
                .height(48.dp)
                .background(colorResource(id = R.color.green500), shape = RoundedCornerShape(8.dp))
                .wrapContentHeight(Alignment.CenterVertically),
        )
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapView(modifier: Modifier, location: LatLng) {
    val cameraPositionState = rememberCameraPositionState()
    LaunchedEffect(location) {
        cameraPositionState.position = CameraPosition(location, cameraPositionState.position.zoom)
    }
    NaverMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(locationTrackingMode = LocationTrackingMode.None),
        uiSettings = MapUiSettings(isZoomControlEnabled = false, isLocationButtonEnabled = false),
        onLocationChange = {

        },
    ) {
    }
}

fun String.getTodayOpenTime(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val openTime = formatter.parse(this)
    val currentTime = Date()
    val durationInMillis = currentTime.time - (openTime?.time ?: 0)
    val hours = durationInMillis / (60 * 60 * 1000)
    val minutes = durationInMillis % (60 * 60 * 1000) / (60 * 1000)
    val seconds = durationInMillis % (60 * 1000) / 1000
    return String.format("%d시 %02d분 %02d초", hours, minutes, seconds)
}