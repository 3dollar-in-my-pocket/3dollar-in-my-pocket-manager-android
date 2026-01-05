package app.threedollars.manager.feature.review.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Gray10
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Gray20
import app.threedollars.common.ui.Gray30
import app.threedollars.common.ui.Gray40
import app.threedollars.common.ui.Gray50
import app.threedollars.common.ui.Gray80
import app.threedollars.common.ui.Gray90
import app.threedollars.common.ui.Gray95
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.Red
import app.threedollars.common.ui.White
import app.threedollars.manager.feature.review.DialogType
import app.threedollars.manager.feature.review.DialogType.PRESET_DIALOG
import app.threedollars.manager.feature.review.DialogType.PRESET_EDIT_DIALOG
import app.threedollars.manager.feature.review.DialogType.PRESET_WRITE_DIALOG
import app.threedollars.manager.feature.review.DialogType.REPORT_DIALOG
import app.threedollars.manager.feature.review.R
import app.threedollars.manager.feature.review.ScreenType
import app.threedollars.manager.feature.review.ScreenType.ALL_REVIEW
import app.threedollars.manager.feature.review.model.CommentPresetVo
import app.threedollars.manager.feature.review.model.ReviewVo
import app.threedollars.manager.feature.review.noRippleClickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReviewDetailScreen(
    reviewVo: ReviewVo,
    commentPresets: List<CommentPresetVo>,
    dialogType: DialogType,
    selectEditPresetText: String,
    selectEditPresetId: String,
    onScreenTypeUpdate: (ScreenType) -> Unit,
    onDialogTypeUpdate: (DialogType) -> Unit,
    onReportClick: (String, () -> Unit) -> Unit,
    onCommentClick: (String) -> Unit,
    onCommentDeleteClick: () -> Unit,
    onPresetClick: () -> Unit,
    onPresetWriteClick: (String) -> Unit,
    onPresetEditClick: (String, String) -> Unit,
    onPresetDeleteClick: (String) -> Unit,
    onPresetEditMenuClick: (String, String) -> Unit,
    onStickerClick: (String, String, Boolean) -> Unit,
) {
    val commentFocusRequester = remember { FocusRequester() }
    val isCommentFocused = remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }

    val commentFocusModifier = Modifier
        .focusRequester(commentFocusRequester)
        .onFocusChanged { isCommentFocused.value = it.isFocused }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            ReviewDetailTopBar(
                onScreenTypeUpdate = onScreenTypeUpdate,
                onDialogTypeUpdate = onDialogTypeUpdate
            )
        },
        bottomBar = {
            if (reviewVo.comment == null) {
                BottomButton(
                    enabled = commentText.length > 9,
                    onPressed = {
                        onCommentClick.invoke(commentText)
                    }
                )
            }
        }
    ) { paddingValues ->

        when (dialogType) {
            REPORT_DIALOG -> {
                ReportBottomSheetDialog(
                    sheetState = sheetState,
                    onDialogTypeUpdate = onDialogTypeUpdate,
                    onReportClick = onReportClick,
                    onReportComplete = { onScreenTypeUpdate(ALL_REVIEW) }
                )
            }

            PRESET_DIALOG -> {
                PresetBottomSheetDialog(
                    sheetState = sheetState,
                    commentPresets = commentPresets,
                    onDialogTypeUpdate = onDialogTypeUpdate,
                    onPresetItemClick = {
                        commentText = it
                    },
                    onPresetEditMenuClick = onPresetEditMenuClick,
                    onPresetDeleteClick = onPresetDeleteClick
                )
            }

            PRESET_WRITE_DIALOG -> {
                PresetWriteBottomSheetDialog(
                    sheetState = sheetState,
                    onDialogTypeUpdate = onDialogTypeUpdate,
                    onPresetWriteClick = onPresetWriteClick
                )
            }

            PRESET_EDIT_DIALOG -> {
                PresetEditBottomSheetDialog(
                    sheetState = sheetState,
                    presetText = selectEditPresetText,
                    presetId = selectEditPresetId,
                    onDialogTypeUpdate = onDialogTypeUpdate,
                    onPresetEditClick = onPresetEditClick
                )
            }

            else -> {}
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.End
        ) {
            ReviewCardView(
                reviewVo = reviewVo,
                onStickerClick = { onStickerClick(reviewVo.reviewId, if (reviewVo.sticker.reactedByMe) "" else "LIKE", true) }
            )
            if (reviewVo.comment == null) {
                CommentView(
                    commentFocusRequester = commentFocusRequester,
                    isCommentFocused = isCommentFocused,
                    commentFocusModifier = commentFocusModifier,
                    commentText = commentText,
                    onCommentTextChanged = { newText ->
                        commentText = newText
                    },
                    onPresetClick = onPresetClick
                )
            } else {
                Row(
                    modifier = Modifier
                        .padding(
                            horizontal = 20.dp,
                            vertical = 16.dp
                        )
                        .noRippleClickable { onCommentDeleteClick() },
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_delete),
                        contentDescription = ""
                    )

                    Text(
                        text = "답글 삭제하기",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Gray80
                    )
                }
            }
        }
    }
}

@Composable
private fun CommentView(
    commentFocusRequester: FocusRequester,
    isCommentFocused: MutableState<Boolean>,
    commentFocusModifier: Modifier,
    commentText: String,
    onCommentTextChanged: (String) -> Unit,
    onPresetClick: () -> Unit,
) {
    val commentTextLimit = 300

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray20)
        )

        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = "리뷰에 답글을 달아주세요!",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Gray100
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = "부적절한 내용의 답글일 경우 삭제될 수 있습니다.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Gray50
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(top = 16.dp)
                .noRippleClickable { commentFocusRequester.requestFocus() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Gray10),
            border = BorderStroke(
                width = 1.dp,
                color = if (isCommentFocused.value) Green else Gray10
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    modifier = commentFocusModifier
                        .fillMaxWidth()
                        .height(240.dp),
                    value = commentText,
                    onValueChange = { newText ->
                        if (newText.length <= commentTextLimit) {
                            onCommentTextChanged(newText)
                        }
                    },
                    placeholder = {
                        Text(
                            text = "소중한 리뷰 감사드립니다!",
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
                                color = if (commentText.length < 10) {
                                    Gray50
                                } else {
                                    Green
                                }
                            )
                        ) {
                            append(commentText.length.toString())
                        }
                        append("/${commentTextLimit}")
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
            onClick = {
                onPresetClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Gray10
            )
        ) {
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = "자주 쓰는 문구",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Gray90
            )
        }
    }
}

@Composable
private fun ReviewDetailTopBar(
    onScreenTypeUpdate: (ScreenType) -> Unit,
    onDialogTypeUpdate: (DialogType) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 34.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier
                .noRippleClickable { onScreenTypeUpdate(ALL_REVIEW) },
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.noRippleClickable {
                onDialogTypeUpdate(REPORT_DIALOG)
            },
            text = "리뷰 신고하기",
            color = Red,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun BottomButton(
    enabled: Boolean,
    onPressed: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(enabled) {
                onPressed.invoke()
            }
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .background(
                color = if (enabled) {
                    Green
                } else {
                    Gray30
                },
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "답글 등록하기",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = White,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(
    name = "ReviewDetailScreen",
    showBackground = true,
)
@Composable
fun PreviewReviewDetailScreen_EmptyComment() {
    val dummyReview = ReviewVo(
        reviewId = "rev1",
        rating = 4,
        contents = "아주 맛있었어요!",
        images = listOf(),
        writer = ReviewVo.Writer(name = "홍길동", medal = ReviewVo.Writer.Medal()),
        comment = ReviewVo.Comment(
            commentId = "111",
            content = "내용",
            status = "",
        )
    )
    ReviewDetailScreen(
        reviewVo = dummyReview,
        commentPresets = listOf(),
        dialogType = DialogType.NONE,
        selectEditPresetText = "",
        selectEditPresetId = "",
        onScreenTypeUpdate = {},
        onDialogTypeUpdate = {},
        onReportClick = { _, _ -> },
        onCommentClick = { },
        onCommentDeleteClick = {},
        onPresetClick = {},
        onPresetWriteClick = {},
        onPresetEditClick = { _, _ -> },
        onPresetDeleteClick = {},
        onPresetEditMenuClick = { _, _ -> },
        onStickerClick = { _, _, _ -> }
    )
}

@Preview(
    name = "ReviewDetailScreen",
    showBackground = true,
)
@Composable
fun PreviewReviewDetailScreen_NullComment() {
    val dummyReview = ReviewVo(
        reviewId = "rev1",
        rating = 4,
        contents = "아주 맛있었어요!",
        images = listOf(),
        writer = ReviewVo.Writer(name = "홍길동", medal = ReviewVo.Writer.Medal()),
        comment = null
    )
    ReviewDetailScreen(
        reviewVo = dummyReview,
        commentPresets = listOf(),
        dialogType = DialogType.NONE,
        selectEditPresetText = "",
        selectEditPresetId = "",
        onScreenTypeUpdate = {},
        onDialogTypeUpdate = {},
        onReportClick = { _, _ -> },
        onCommentClick = { },
        onCommentDeleteClick = {},
        onPresetClick = {},
        onPresetWriteClick = {},
        onPresetEditClick = { _, _ -> },
        onPresetDeleteClick = {},
        onPresetEditMenuClick = { _, _ -> },
        onStickerClick = { _, _, _ -> }
    )
}
