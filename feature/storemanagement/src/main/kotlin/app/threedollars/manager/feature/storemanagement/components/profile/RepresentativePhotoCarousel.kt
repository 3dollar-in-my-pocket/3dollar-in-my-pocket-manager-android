package app.threedollars.manager.feature.storemanagement.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Gray10
import app.threedollars.common.ui.White
import app.threedollars.common.ui.photo.PhotoViewerPosition
import app.threedollars.manager.feature.storemanagement.R
import coil.compose.AsyncImage

private const val COUNTER_BOTTOM_PADDING = 42

@Composable
internal fun RepresentativePhotoCarousel(
    imageUrls: List<String>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.background(Gray10)) {
        if (imageUrls.isEmpty()) return@Box

        val pagerState = rememberPagerState(initialPage = 0) { imageUrls.size }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            AsyncImage(
                model = imageUrls[page],
                contentDescription = stringResource(id = R.string.store_photo_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        val position = PhotoViewerPosition(index = pagerState.currentPage, total = imageUrls.size)
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = COUNTER_BOTTOM_PADDING.dp)
                .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_camera),
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.store_photo_carousel_counter, position.displayIndex, position.total),
                fontSize = 12.sp,
                color = White,
            )
        }
    }
}
