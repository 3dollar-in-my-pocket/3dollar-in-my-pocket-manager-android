package app.threedollars.manager.feature.review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Gray40
import app.threedollars.common.ui.Gray50
import app.threedollars.common.ui.Gray80
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.White
import app.threedollars.manager.feature.review.DialogType
import app.threedollars.manager.feature.review.R
import app.threedollars.manager.feature.review.model.CommentPresetVo
import app.threedollars.manager.feature.review.noRippleClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PresetBottomSheetDialog(
    sheetState: SheetState,
    commentPresets: List<CommentPresetVo>,
    onDialogTypeUpdate: (DialogType) -> Unit,
    onPresetItemClick: (String) -> Unit,
    onPresetEditMenuClick: (String, String) -> Unit,
    onPresetDeleteClick: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDialogTypeUpdate(DialogType.NONE) },
        sheetState = sheetState,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 20.dp
                    )
                    .imePadding()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "자주 쓰는 문구",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Gray100
                    )

                    Image(
                        modifier = Modifier.noRippleClickable {
                            onDialogTypeUpdate(DialogType.NONE)
                        },
                        imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                        contentDescription = ""
                    )
                }
                Spacer(modifier = Modifier.height(28.dp))
                if (commentPresets.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 28.dp),
                        text = "자주 사용하는 문구를 등록하면\n편리하고 빠르게 답글을 달 수 있어요!",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Gray50,
                        textAlign = TextAlign.Center
                    )
                } else {
                    commentPresets.forEach {
                        PresetItemView(
                            presetText = it.body,
                            presetId = it.presetId,
                            onPresetItemClick = { presetText ->
                                onPresetItemClick(presetText)
                                onDialogTypeUpdate(DialogType.NONE)
                            },
                            onPresetEditMenuClick = onPresetEditMenuClick,
                            onPresetDeleteClick = onPresetDeleteClick
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                Button(
                    onClick = { onDialogTypeUpdate(DialogType.PRESET_WRITE_DIALOG) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = commentPresets.size < 5,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green,
                        disabledContainerColor = Gray40
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 4.dp),
                        text = "문구 추가",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = White
                    )
                }
            }
        }
    )
}

@Composable
private fun PresetItemView(
    presetText: String,
    presetId: String,
    onPresetItemClick: (String) -> Unit,
    onPresetEditMenuClick: (String, String) -> Unit,
    onPresetDeleteClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .weight(9f)
                .noRippleClickable {
                    onPresetItemClick(presetText)
                },
            text = presetText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Gray80
        )

        PresetMenuView(
            modifier = Modifier.weight(1f),
            onPresetEditMenuClick = {
                onPresetEditMenuClick(
                    presetId, presetText
                )
            },
            onPresetDeleteClick = {
                onPresetDeleteClick(
                    presetId
                )
            }
        )
    }
}

@Composable
private fun PresetMenuView(
    modifier: Modifier = Modifier,
    onPresetEditMenuClick: () -> Unit,
    onPresetDeleteClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.noRippleClickable { expanded = true },
            imageVector = ImageVector.vectorResource(R.drawable.ic_more),
            contentDescription = ""
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.padding(end = 20.dp),
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "수정하기",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Gray80
                    )
                },
                onClick = {
                    expanded = false
                    onPresetEditMenuClick()
                },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_edit_line),
                        contentDescription = null
                    )
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = "삭제하기",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Gray80
                    )
                },
                onClick = {
                    expanded = false
                    onPresetDeleteClick()
                },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                        contentDescription = null
                    )
                }
            )
        }
    }
}