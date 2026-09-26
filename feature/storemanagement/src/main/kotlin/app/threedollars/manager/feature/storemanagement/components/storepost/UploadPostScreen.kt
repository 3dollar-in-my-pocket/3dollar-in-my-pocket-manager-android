package app.threedollars.manager.feature.storemanagement.components.storepost

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.threedollars.common.BaseDialog
import app.threedollars.common.analytics.ScreenViewLogEffect
import app.threedollars.common.ui.CircleProgressBar
import app.threedollars.common.ui.Gray0
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Gray30
import app.threedollars.common.ui.Gray40
import app.threedollars.common.ui.Gray5
import app.threedollars.common.ui.Gray80
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.White
import app.threedollars.manager.feature.storemanagement.ContentUriToRequestBody
import app.threedollars.manager.feature.storemanagement.R
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun UploadPostRoute(
    onBack: () -> Unit,
) {
    val viewModel: StorePostViewModel = hiltViewModel()
    val uploadState by viewModel.uploadStateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showCancelDialog by remember { mutableStateOf(false) }

    ScreenViewLogEffect(screenName = "uploadPost")

    LaunchedEffect(Unit) {
        viewModel.uploadFinishedFlow.collectLatest { onBack() }
    }

    val requestBack: () -> Unit = {
        if (uploadState.isDirty && !uploadState.isSaving) showCancelDialog = true else onBack()
    }
    BackHandler(onBack = requestBack)

    if (showCancelDialog) {
        BaseDialog(
            title = "",
            message = "작성 중인 소식을 취소하시겠습니까?",
            confirmText = "취소하기",
            dismissText = "계속 작성",
            onConfirm = {
                showCancelDialog = false
                onBack()
            },
            onDismiss = { showCancelDialog = false },
        )
    }

    UploadPostScreen(
        uploadState = uploadState,
        onBackClick = requestBack,
        onBodyChange = viewModel::updateBody,
        onPhotosPicked = { uris -> viewModel.addPhotos(uris.map { it.toLocalPhoto(context) }) },
        onPhotoRemove = viewModel::removePhoto,
        onSaveClick = {
            viewModel.save { local -> ContentUriToRequestBody(context, Uri.parse(local.uri)) }
        },
        onErrorDismiss = viewModel::clearUploadError,
    )
}

private fun Uri.toLocalPhoto(context: Context): UploadPhoto.Local {
    val ratio = runCatching {
        context.contentResolver.openInputStream(this)?.use { stream ->
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeStream(stream, null, options)
            if (options.outWidth > 0 && options.outHeight > 0) {
                options.outWidth.toDouble() / options.outHeight.toDouble()
            } else {
                null
            }
        }
    }.getOrNull() ?: 1.0
    return UploadPhoto.Local(uri = toString(), ratio = ratio)
}

@Composable
internal fun UploadPostScreen(
    uploadState: UploadPostState,
    onBackClick: () -> Unit,
    onBodyChange: (String) -> Unit,
    onPhotosPicked: (List<Uri>) -> Unit,
    onPhotoRemove: (Int) -> Unit,
    onSaveClick: () -> Unit,
    onErrorDismiss: () -> Unit,
) {
    if (uploadState.errorMessage != null) {
        BaseDialog(
            title = "Error",
            message = uploadState.errorMessage,
            confirmText = "확인",
            onConfirm = onErrorDismiss,
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(White)) {
        Column(modifier = Modifier.fillMaxSize()) {
            UploadPostHeader(onBackClick = onBackClick)

            PhotoPickerRow(
                photos = uploadState.photos,
                remainingCount = uploadState.remainingPhotoCount,
                onPhotosPicked = onPhotosPicked,
                onPhotoRemove = onPhotoRemove,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "소식",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray100,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Gray80)) { append("${uploadState.body.length}") }
                        append("/$UPLOAD_POST_MAX_BODY_LENGTH")
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray40,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp)
                    .height(240.dp)
                    .background(Gray5, RoundedCornerShape(8.dp))
                    .padding(horizontal = 15.dp, vertical = 12.dp),
            ) {
                if (uploadState.body.isEmpty()) {
                    Text(
                        text = "고객들에게 알리고 싶은 소식을 적어주세요!",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Gray30,
                    )
                }
                BasicTextField(
                    value = uploadState.body,
                    onValueChange = onBodyChange,
                    modifier = Modifier.fillMaxSize(),
                    textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Gray100),
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(0.dp),
                enabled = uploadState.isSaveEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green,
                    contentColor = White,
                    disabledContainerColor = Gray30,
                    disabledContentColor = White,
                ),
                onClick = onSaveClick,
            ) {
                Text(text = "저장하기", fontSize = 16.sp, fontWeight = FontWeight.W500)
            }
        }

        if (uploadState.isSaving) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {},
            ) {
                CircleProgressBar(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun UploadPostHeader(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray0, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
            .padding(horizontal = 24.dp, vertical = 24.dp),
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable(onClick = onBackClick),
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "뒤로가기",
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "소식 올리기",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Gray100,
        )
    }
}

@Composable
private fun PhotoPickerRow(
    photos: List<UploadPhoto>,
    remainingCount: Int,
    onPhotosPicked: (List<Uri>) -> Unit,
    onPhotoRemove: (Int) -> Unit,
) {
    val multiContract = remember(remainingCount) {
        ActivityResultContracts.PickMultipleVisualMedia(maxItems = remainingCount.coerceAtLeast(2))
    }
    val multiLauncher = rememberLauncherForActivityResult(multiContract) { uris ->
        if (uris.isNotEmpty()) onPhotosPicked(uris.take(remainingCount))
    }
    val singleLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) onPhotosPicked(listOf(uri))
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Column(
                modifier = Modifier
                    .size(72.dp)
                    .background(Gray5, RoundedCornerShape(16.dp))
                    .clickable(enabled = remainingCount > 0) {
                        val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        if (remainingCount == 1) singleLauncher.launch(request) else multiLauncher.launch(request)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_camera),
                    contentDescription = "사진 추가",
                    modifier = Modifier.size(28.dp),
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = Green, fontWeight = FontWeight.Bold)) { append("${photos.size}") }
                        append("/$UPLOAD_POST_MAX_PHOTO")
                    },
                    fontSize = 12.sp,
                    color = Gray40,
                )
            }
        }
        itemsIndexed(photos) { index, photo ->
            Box(modifier = Modifier.size(72.dp)) {
                AsyncImage(
                    model = when (photo) {
                        is UploadPhoto.Remote -> photo.url
                        is UploadPhoto.Local -> Uri.parse(photo.uri)
                    },
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                )
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_close_24),
                    contentDescription = "사진 삭제",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-4).dp)
                        .size(20.dp)
                        .clickable { onPhotoRemove(index) },
                )
            }
        }
    }
}
