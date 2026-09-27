package app.threedollars.manager.feature.storemanagement.components.storepost

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Gray40
import app.threedollars.common.ui.Gray50
import app.threedollars.common.ui.Gray60
import app.threedollars.common.ui.Gray80
import app.threedollars.common.ui.Gray95
import app.threedollars.common.ui.White
import app.threedollars.domain.dto.StorePostDto
import app.threedollars.domain.dto.StorePostSectionType
import app.threedollars.manager.feature.storemanagement.R
import coil.compose.AsyncImage

private val POST_IMAGE_HEIGHT = 208.dp

@Composable
internal fun StorePostCard(
    post: StorePostDto,
    onEditClick: (StorePostDto) -> Unit,
    onDeleteClick: (StorePostDto) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val images = post.sections.filter { it.sectionType == StorePostSectionType.IMAGE }
    val likeCount = post.stickers.firstOrNull()?.count ?: 0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(16.dp))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = post.store.categories.firstOrNull()?.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
            ) {
                Text(
                    text = post.store.storeName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray100,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = RelativeTimeFormatter.format(post.createdAt),
                    fontSize = 12.sp,
                    color = Gray40,
                )
            }
            Box {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_more),
                    contentDescription = "더보기",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { menuExpanded = true },
                )
                PostMoreMenu(
                    expanded = menuExpanded,
                    onDismiss = { menuExpanded = false },
                    onEdit = {
                        menuExpanded = false
                        onEditClick(post)
                    },
                    onDelete = {
                        menuExpanded = false
                        onDeleteClick(post)
                    },
                )
            }
        }

        if (images.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clipToBounds(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                images.forEach { section ->
                    val ratio = if (section.ratio > 0) section.ratio else 1.0
                    AsyncImage(
                        model = section.url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(POST_IMAGE_HEIGHT)
                            .width(POST_IMAGE_HEIGHT * ratio.toFloat())
                            .clip(RoundedCornerShape(8.dp)),
                    )
                }
            }
        }

        Text(
            text = post.body,
            fontSize = 14.sp,
            color = Gray95,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_heart_fill),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Gray50),
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = "좋아요 $likeCount",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Gray60,
            )
        }
    }
}

@Composable
private fun PostMoreMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .width(132.dp)
            .background(White),
        shape = RoundedCornerShape(8.dp),
        containerColor = White,
    ) {
        PostMoreMenuItem(text = "수정하기", iconRes = R.drawable.ic_write_line, onClick = onEdit)
        PostMoreMenuItem(text = "삭제하기", iconRes = R.drawable.ic_delete, onClick = onDelete)
    }
}

@Composable
private fun PostMoreMenuItem(text: String, iconRes: Int, onClick: () -> Unit) {
    DropdownMenuItem(
        modifier = Modifier.height(42.dp),
        text = {
            Text(text = text, fontSize = 12.sp, color = Gray80)
        },
        trailingIcon = {
            Image(
                imageVector = ImageVector.vectorResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(16.dp),
            )
        },
        onClick = onClick,
    )
}
