package app.threedollars.manager.feature.review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import app.threedollars.manager.feature.review.noRippleClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReportBottomSheetDialog(
    sheetState: SheetState,
    onDialogTypeUpdate: (DialogType) -> Unit,
    onReportClick: (String) -> Unit
) {
    val reportFocusRequester = remember { FocusRequester() }
    var reportText by remember { mutableStateOf("") }
    val reportTextLimit = 300

    val reportFocusModifier = Modifier
        .focusRequester(reportFocusRequester)

    ModalBottomSheet(
        onDismissRequest = { onDialogTypeUpdate(DialogType.NONE) },
        sheetState = sheetState,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .padding(bottom = 40.dp)
                    .imePadding()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "신고사유를 입력해주세요.",
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

                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = "관리자 판단 후 리뷰를 삭제해드립니다.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Gray50
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(top = 16.dp)
                        .noRippleClickable { reportFocusRequester.requestFocus() },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Gray10),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextField(
                            modifier = reportFocusModifier
                                .fillMaxWidth()
                                .height(240.dp),
                            value = reportText,
                            onValueChange = { newText ->
                                if (newText.length <= reportTextLimit) {
                                    reportText = newText
                                }
                            },
                            placeholder = {
                                Text(
                                    text = "신고사유 입력",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Gray40
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Gray10,
                                unfocusedContainerColor = Gray10,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Green,
                                focusedTextColor = Gray95,
                                unfocusedTextColor = Gray95
                            ),
                        )

                        Text(
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 10.dp
                            ),
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = if (reportText.length < 10) {
                                            Gray50
                                        } else {
                                            Green
                                        }
                                    )
                                ) {
                                    append(reportText.length.toString())
                                }
                                append("/${reportTextLimit}")
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Gray50
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "*최소 10자에서 최대 300자 이내로 입력해 주세요.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray50
                )

                Button(
                    onClick = { onReportClick(reportText) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = reportText.length > 9,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Red,
                        disabledContainerColor = Gray30
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 4.dp),
                        text = "신고하기",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = White
                    )
                }
            }
        }
    )
}