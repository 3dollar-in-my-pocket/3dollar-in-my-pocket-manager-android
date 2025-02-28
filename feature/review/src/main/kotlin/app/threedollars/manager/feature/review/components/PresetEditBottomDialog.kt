package app.threedollars.manager.feature.review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Gray10
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Gray30
import app.threedollars.common.ui.Gray40
import app.threedollars.common.ui.Gray50
import app.threedollars.common.ui.Gray95
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.Red
import app.threedollars.common.ui.White
import app.threedollars.manager.feature.review.DialogType
import app.threedollars.manager.feature.review.R
import app.threedollars.manager.feature.review.model.CommentPresetVo
import app.threedollars.manager.feature.review.noRippleClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PresetEditBottomSheetDialog(
    sheetState: SheetState,
    commentPresets: List<CommentPresetVo>,
    onDialogTypeUpdate: (DialogType) -> Unit,
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

                }

                Button(
                    onClick = { onDialogTypeUpdate(DialogType.PRESET_WRITE_DIALOG) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = commentPresets.size < 6,
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