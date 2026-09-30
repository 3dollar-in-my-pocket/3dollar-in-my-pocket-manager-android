package app.threedollars.manager.feature.storemanagement.components.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ext.toast
import app.threedollars.common.ui.Black
import app.threedollars.common.ui.Gray40
import app.threedollars.common.ui.Gray5
import app.threedollars.common.ui.Gray50
import app.threedollars.common.ui.Green
import app.threedollars.manager.feature.storemanagement.R
import coil.compose.AsyncImage

private const val PREVIEW_ASPECT_RATIO = 327f / 204f

@Composable
internal fun RepresentativePhotoSection(
    state: RepresentativePhotoState,
    onPhotosAdd: (List<String>) -> Unit,
    onPhotoDelete: (Int) -> Unit,
) {
    val context = LocalContext.current
    val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    val multiplePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(maxItems = state.remainingCount.coerceAtLeast(2))
    ) { uris ->
        onPhotosAdd(uris.map { it.toString() })
    }
    val singlePicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) onPhotosAdd(listOf(uri.toString()))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, top = 12.dp, end = 24.dp, bottom = 48.dp)
    ) {
        RepresentativePreview(imageModel = state.photos.firstOrNull()?.model)
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 4.dp, end = 4.dp)
        ) {
            item {
                PhotoAddCell(
                    count = state.count,
                    onClick = {
                        when {
                            !state.canAdd -> context.toast(R.string.store_photo_max_toast)
                            state.remainingCount == 1 -> singlePicker.launch(request)
                            else -> multiplePicker.launch(request)
                        }
                    }
                )
            }
            itemsIndexed(state.photos) { index, photo ->
                PhotoThumbnail(
                    imageModel = photo.model,
                    canDelete = state.canDelete,
                    onDelete = { onPhotoDelete(index) }
                )
            }
        }
    }
}

@Composable
private fun RepresentativePreview(imageModel: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(PREVIEW_ASPECT_RATIO)
            .clip(RoundedCornerShape(12.dp))
            .background(Gray5)
    ) {
        if (imageModel != null) {
            AsyncImage(
                model = imageModel,
                contentDescription = stringResource(id = R.string.store_photo_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Black)
                    .padding(horizontal = 6.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check_green_20),
                    contentDescription = null,
                    tint = Gray40,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = stringResource(id = R.string.store_photo_representative_badge),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray40,
                )
            }
        }
    }
}

@Composable
private fun PhotoAddCell(
    count: Int,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Gray5)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_camera),
            contentDescription = stringResource(id = R.string.store_photo_add_description),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = count.toString(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Green,
            )
            Text(
                text = stringResource(id = R.string.store_photo_count_max, MAX_REPRESENTATIVE_PHOTO_COUNT),
                fontSize = 12.sp,
                color = Gray50,
            )
        }
    }
}

@Composable
private fun PhotoThumbnail(
    imageModel: String,
    canDelete: Boolean,
    onDelete: () -> Unit,
) {
    Box(modifier = Modifier.size(72.dp)) {
        AsyncImage(
            model = imageModel,
            contentDescription = stringResource(id = R.string.store_photo_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        )
        if (canDelete) {
            Image(
                painter = painterResource(id = R.drawable.ic_delete_circle),
                contentDescription = stringResource(id = R.string.store_photo_delete_description),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable(onClick = onDelete)
                    .padding(4.dp)
                    .size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RepresentativePhotoSectionPreview() {
    RepresentativePhotoSection(
        state = RepresentativePhotoState.from(listOf("a", "b")),
        onPhotosAdd = {},
        onPhotoDelete = {},
    )
}
