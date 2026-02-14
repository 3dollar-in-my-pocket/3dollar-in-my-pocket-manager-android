package app.threedollars.manager.feature.storemanagement.components.profile

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.BaseDialog
import app.threedollars.common.ext.toStringDefault
import app.threedollars.common.ui.FlowRow
import app.threedollars.common.ui.Gray0
import app.threedollars.common.ui.Gray30
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.MildGreen
import app.threedollars.common.util.PhoneNumberUtils
import app.threedollars.manager.feature.storemanagement.DialogType
import app.threedollars.manager.feature.storemanagement.R
import app.threedollars.manager.feature.storemanagement.ScreenType
import app.threedollars.manager.feature.storemanagement.model.BossStorePatchModel
import app.threedollars.manager.feature.storemanagement.model.BossStoreRetrieveVo
import app.threedollars.manager.feature.storemanagement.model.StoreCategoriesVo
import okhttp3.RequestBody

@Composable
internal fun ProfileEditScreen(
    bossStoreRetrieve: BossStoreRetrieveVo,
    storeCategories: List<StoreCategoriesVo>,
    selectedStoreCategories: List<String>,
    onScreenTypeUpdate: (ScreenType) -> Unit,
    onBossStorePatch: (BossStorePatchModel) -> Unit,
    onStoreCategorySelected: (Int) -> Unit,
) {
    var isEnable by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf(bossStoreRetrieve.name.toStringDefault()) }
    var imageRequestBody by remember { mutableStateOf<RequestBody?>(null) }
    var sns by remember { mutableStateOf(bossStoreRetrieve.snsUrl.toStringDefault()) }
    var contactNumberValue by remember {
        val initialText = bossStoreRetrieve.contactNumbers.firstOrNull()?.number.toStringDefault()
        mutableStateOf(
            TextFieldValue(
                text = initialText,
                selection = TextRange(initialText.length)
            )
        )
    }
    val imageUrl by remember { mutableStateOf(bossStoreRetrieve.imageUrl.toStringDefault()) }

    val originalContactNumber = bossStoreRetrieve.contactNumbers.firstOrNull()?.number.toStringDefault()
    val isContactNumberChanged = contactNumberValue.text != originalContactNumber
    val isContactNumberValid = contactNumberValue.text.isEmpty() || PhoneNumberUtils.isValidPhoneNumber(contactNumberValue.text)

    isEnable = ((name.isNotEmpty() && name != bossStoreRetrieve.name) ||
            (selectedStoreCategories.isNotEmpty() && selectedStoreCategories != bossStoreRetrieve.categories.map { it.categoryId }) ||
            imageRequestBody != null ||
            sns != bossStoreRetrieve.snsUrl ||
            isContactNumberChanged) && isContactNumberValid


    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(Gray0)
    ) {
        ProfileEditTop(
            onScreenTypeUpdate = onScreenTypeUpdate
        )
        ProfileEditContents(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topStart = 32.dp,
                        topEnd = 32.dp
                    )
                ),
            storeCategories = storeCategories,
            name = name,
            imageUrl = imageUrl,
            sns = sns,
            contactNumberValue = contactNumberValue,
            onChangeName = { name = it },
            onChangeUri = { imageRequestBody = it },
            onChangeSNS = { sns = it },
            onChangeContactNumber = { contactNumberValue = it },
            onStoreCategorySelected = onStoreCategorySelected
        )

        ProfileEditBottom(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            isEnable = isEnable,
            onClick = {
                val validContactNumber = if (PhoneNumberUtils.isValidPhoneNumber(contactNumberValue.text)) {
                    PhoneNumberUtils.toApiFormat(contactNumberValue.text)
                } else ""

                onBossStorePatch(
                    BossStorePatchModel(
                        bossStoreId = bossStoreRetrieve.bossStoreId,
                        name = name,
                        snsUrl = sns,
                        categoriesIds = selectedStoreCategories,
                        imageRequestBody = imageRequestBody,
                        contactNumber = validContactNumber
                    )
                )
            }
        )
    }
}

@Composable
internal fun ProfileEditTop(
    onScreenTypeUpdate: (ScreenType) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(modifier = Modifier
            .wrapContentWidth()
            .padding(start = 16.dp),
            onClick = {
                onScreenTypeUpdate(ScreenType.STORE_INFO)
            }) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = ""
            )
        }
        Text(
            modifier = Modifier.fillMaxWidth(0.8f),
            text = "대표 정보 수정",
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
        )
    }
}

@Preview
@Composable
fun ProfileEditBottom(
    modifier: Modifier = Modifier,
    isEnable: Boolean = false,
    onClick: () -> Unit = {},
) {
    Button(
        modifier = modifier.height(64.dp),
        onClick = { if (isEnable) onClick() },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = if (isEnable) Green else Gray30
        ),
        content = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "저장하기",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    )
}


@Composable
fun ProfileEditContents(
    modifier: Modifier = Modifier,
    storeCategories: List<StoreCategoriesVo>,
    name: String,
    imageUrl: String,
    sns: String,
    contactNumberValue: TextFieldValue,
    onChangeName: (String) -> Unit,
    onChangeUri: (RequestBody) -> Unit,
    onChangeSNS: (String) -> Unit,
    onChangeContactNumber: (TextFieldValue) -> Unit,
    onStoreCategorySelected: (Int) -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scrollState)
    ) {
        StoreNameTextFieldContent(
            name = name,
            onChangeText = onChangeName
        )
        ProFileTitleTextContent(
            titleText = "카테고리 선택",
            explanationText = "최대 3개",
            isExplanationText = true
        )
        CategoryGrid(
            storeCategories = storeCategories,
            onStoreCategorySelected = onStoreCategorySelected
        )
        ProFileTitleTextContent(
            titleText = "가게 인증 사진",
            isExplanationText = false
        )
        ProfileCertificationPhoto(
            defaultImage = Uri.parse(imageUrl),
            onChangeUri = onChangeUri
        )
        ProFileTitleTextContent(
            titleText = "SNS",
            isExplanationText = false,
            isRequired = false
        )
        DefaultTextFieldContent(
            default = sns,
            hint = "SNS를 입력해 주세요.",
            maxLength = 50,
            onChangeText = onChangeSNS
        )
        ProFileTitleTextContent(
            titleText = "연락처",
            isExplanationText = false,
            isRequired = false
        )
        PhoneNumberTextField(
            phoneNumberValue = contactNumberValue,
            onPhoneNumberChange = onChangeContactNumber
        )
        Spacer(modifier = Modifier.height(44.dp))
    }
}

@Preview
@Composable
fun StoreNameTextFieldContent(
    name: String = "",
    onChangeText: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ProFileTitleTextContent(
            titleText = "가게 이름",
            explanationText = "공백 포함 최대 20자",
            isExplanationText = true
        )
        DefaultTextFieldContent(
            default = name,
            hint = "가게 이름을 입력해 주세요.",
            onChangeText = onChangeText,
            imeAction = ImeAction.Next
        )
    }
}

@Composable
fun CategoryGrid(
    storeCategories: List<StoreCategoriesVo>,
    onStoreCategorySelected: (Int) -> Unit,
) {
    FlowRow(
        horizontalGap = 8.dp,
        modifier = Modifier.padding(16.dp)
    ) {
        storeCategories.forEachIndexed { index, storeCategoriesVo ->
            Button(
                modifier = Modifier.wrapContentSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
                onClick = { onStoreCategorySelected(index) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = if (storeCategoriesVo.isSelected) Color.White else Green,
                    containerColor = if (storeCategoriesVo.isSelected) Green else MildGreen
                )
            ) {
                Text(
                    text = storeCategoriesVo.name,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun PhoneNumberTextField(
    phoneNumberValue: TextFieldValue,
    onPhoneNumberChange: (TextFieldValue) -> Unit,
) {
    val phoneNumber = phoneNumberValue.text
    val isValid = phoneNumber.isEmpty() || PhoneNumberUtils.isValidPhoneNumber(phoneNumber)
    val isComplete = PhoneNumberUtils.isCompletePhoneNumber(phoneNumber)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        DefaultTextFieldContent(
            value = phoneNumberValue,
            hint = "고객들에게 알리고 싶은 연락처를 적어주세요!",
            maxLength = 13,
            onValueChange = { newValue ->
                if (PhoneNumberUtils.isValidPhoneNumberInput(newValue.text)) {
                    val formattedText = PhoneNumberUtils.formatPhoneNumber(newValue.text)
                    // 포맷팅 후 커서를 맨 뒤로 설정
                    val newTextFieldValue = TextFieldValue(
                        text = formattedText,
                        selection = TextRange(formattedText.length)
                    )
                    onPhoneNumberChange(newTextFieldValue)
                }
            }
        )

        if (phoneNumber.isNotEmpty() && !isValid) {
            Text(
                text = "올바른 휴대폰번호 형식이 아닙니다",
                color = androidx.compose.ui.graphics.Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 24.dp, end = 24.dp)
            )
        } else if (phoneNumber.isNotEmpty() && !isComplete) {
            Text(
                text = "휴대폰번호를 완성해주세요",
                color = Gray30,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 24.dp, end = 24.dp)
            )
        }
    }
}


