package app.threedollars.manager.feature.storemanagement.components.coupon

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.threedollars.common.BaseDialog
import app.threedollars.common.analytics.ScreenViewLogEffect
import app.threedollars.common.ext.toast
import app.threedollars.common.ui.CircleProgressBar
import app.threedollars.common.ui.Gray0
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Gray20
import app.threedollars.common.ui.Gray30
import app.threedollars.common.ui.Gray5
import app.threedollars.common.ui.Gray50
import app.threedollars.common.ui.Gray95
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.Red
import app.threedollars.common.ui.White
import app.threedollars.manager.feature.storemanagement.R
import kotlinx.coroutines.flow.collectLatest
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Composable
internal fun RegisterCouponRoute(
    onBack: () -> Unit,
) {
    val viewModel: CouponViewModel = hiltViewModel()
    val registerState by viewModel.registerStateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ScreenViewLogEffect(screenName = "registerCoupon")

    LaunchedEffect(Unit) {
        viewModel.registerFinishedFlow.collectLatest { onBack() }
    }
    LaunchedEffect(Unit) {
        viewModel.toastFlow.collectLatest { context.toast(it) }
    }
    BackHandler(enabled = !registerState.isSaving, onBack = onBack)

    RegisterCouponScreen(
        state = registerState,
        onBackClick = onBack,
        onNameChange = viewModel::updateName,
        onStartDateChange = viewModel::updateStartDate,
        onEndDateChange = viewModel::updateEndDate,
        onCountOptionSelect = viewModel::selectCountOption,
        onCustomCountChange = viewModel::updateCustomCount,
        onSubmitClick = { viewModel.showRegisterConfirm(true) },
        onConfirmDismiss = { viewModel.showRegisterConfirm(false) },
        onConfirmRegister = viewModel::register,
        onErrorDismiss = viewModel::clearRegisterError,
    )
}

@Composable
internal fun RegisterCouponScreen(
    state: RegisterCouponState,
    onBackClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate) -> Unit,
    onCountOptionSelect: (CouponCountOption) -> Unit,
    onCustomCountChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onConfirmDismiss: () -> Unit,
    onConfirmRegister: () -> Unit,
    onErrorDismiss: () -> Unit,
) {
    var datePickerTarget by remember { mutableStateOf<DateTarget?>(null) }

    if (state.errorMessage != null) {
        BaseDialog(title = "Error", message = state.errorMessage, confirmText = "확인", onConfirm = onErrorDismiss)
    }
    if (state.showConfirmDialog) {
        RegisterConfirmDialog(onDismiss = onConfirmDismiss, onConfirm = onConfirmRegister)
    }
    datePickerTarget?.let { target ->
        CouponDateSheet(
            title = target.title,
            initialDate = if (target == DateTarget.START) state.startDate else state.endDate,
            onDismiss = { datePickerTarget = null },
            onConfirm = { date ->
                if (target == DateTarget.START) onStartDateChange(date) else onEndDateChange(date)
                datePickerTarget = null
            },
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(White)) {
        Column(modifier = Modifier.fillMaxSize()) {
            RegisterHeader(onBackClick = onBackClick)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                SectionTitle(title = "쿠폰명", description = "혜택이 바로 보이도록 40자 이내로 입력해주세요.")
                Spacer(modifier = Modifier.height(12.dp))
                CouponNameField(name = state.name, onNameChange = onNameChange)

                Spacer(modifier = Modifier.height(32.dp))
                SectionTitle(
                    title = "쿠폰 사용 기간",
                    description = buildAnnotatedString {
                        append("이 기간 동안 쿠폰을 발급 받거나 사용할 수 있어요.\n종료일 전 발급 중지는 가능하지만, ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("이미 발급된 쿠폰은 유효") }
                        append("합니다!\n사용 기간은 수정할 수 없습니다.")
                    },
                )
                Spacer(modifier = Modifier.height(12.dp))
                DateRow(label = "사용 시작일", date = state.startDate, onClick = { datePickerTarget = DateTarget.START })
                Spacer(modifier = Modifier.height(8.dp))
                DateRow(label = "사용 종료일", date = state.endDate, onClick = { datePickerTarget = DateTarget.END })

                Spacer(modifier = Modifier.height(32.dp))
                SectionTitle(
                    title = "발급 수량",
                    description = "쿠폰 사용 기간 동안 발급받을 수 있는 쿠폰의 수량을 선택해주세요.\n종료일 전에 쿠폰 수량 소진 시",
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CouponCountOption.entries.forEach { option ->
                        val selected = option == state.countOption
                        Text(
                            text = option.title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) White else Gray50,
                            modifier = Modifier
                                .background(if (selected) Green else Gray5, RoundedCornerShape(8.dp))
                                .clickable { onCountOptionSelect(option) }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        )
                    }
                }
                if (state.countOption == CouponCountOption.CUSTOM) {
                    Spacer(modifier = Modifier.height(12.dp))
                    CustomCountField(value = state.customCountInput, onValueChange = onCustomCountChange)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(0.dp),
                enabled = state.isSubmitEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green,
                    contentColor = White,
                    disabledContainerColor = Gray20,
                    disabledContentColor = White,
                ),
                onClick = onSubmitClick,
            ) {
                Text(text = "쿠폰 발급하기", fontSize = 16.sp, fontWeight = FontWeight.W500)
            }
        }
        if (state.isSaving) {
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

private enum class DateTarget(val title: String) {
    START("사용 시작일"),
    END("사용 종료일"),
}

@Composable
private fun RegisterHeader(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray0)
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
            text = "쿠폰 만들기",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Gray100,
        )
    }
}

@Composable
private fun SectionTitle(title: String, description: String) {
    SectionTitle(title = title, description = buildAnnotatedString { append(description) })
}

@Composable
private fun SectionTitle(title: String, description: androidx.compose.ui.text.AnnotatedString) {
    Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Gray100)
    Spacer(modifier = Modifier.height(6.dp))
    Text(text = description, fontSize = 12.sp, color = Gray50)
}

@Composable
private fun CouponNameField(name: String, onNameChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray5, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (name.isEmpty()) {
                Text(text = "예) 1000원 할인, 5천원 이상 구매시 500원 할인!", fontSize = 14.sp, color = Gray30)
            }
            BasicTextField(
                value = name,
                onValueChange = onNameChange,
                singleLine = true,
                textStyle = TextStyle(fontSize = 14.sp, color = Gray100),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = Green)) { append("${name.length}") }
                append("/$COUPON_NAME_MAX_LENGTH")
            },
            fontSize = 12.sp,
            color = Gray50,
        )
    }
}

@Composable
private fun CustomCountField(value: String, onValueChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray5, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        if (value.isEmpty()) {
            Text(text = "수량을 입력해주세요.", fontSize = 14.sp, color = Gray30)
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(fontSize = 14.sp, color = Gray100),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun DateRow(label: String, date: LocalDate, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray5, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Gray50, modifier = Modifier.weight(1f))
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ic_calendar_line),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(text = CouponFormatter.formatPickerDate(date), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Gray100)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CouponDateSheet(
    title: String,
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
) {
    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
    )
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = White) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "취소", fontSize = 14.sp, color = Gray50, modifier = Modifier.clickable(onClick = onDismiss))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Gray100,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "확인",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Green,
                    modifier = Modifier.clickable {
                        val millis = pickerState.selectedDateMillis
                        if (millis != null) {
                            onConfirm(Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate())
                        } else {
                            onDismiss()
                        }
                    },
                )
            }
            DatePicker(
                state = pickerState,
                title = null,
                headline = null,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = White,
                    selectedDayContainerColor = Green,
                    todayDateBorderColor = Green,
                    todayContentColor = Green,
                ),
            )
        }
    }
}

@Composable
private fun RegisterConfirmDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White, RoundedCornerShape(20.dp))
                .padding(24.dp),
        ) {
            Text(text = "쿠폰 발급을 시작할까요?", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Gray100)
            Spacer(modifier = Modifier.height(16.dp))
            listOf(
                "👀 쿠폰은 발급과 동시에 고객 앱에 노출돼요.",
                "⏰ 고객은 설정한 사용 기간 동안 쿠폰을 사용할 수 있어요.",
                "⚠️ 한 번 발급하면, 사용 기간은 수정할 수 없습니다.",
                "✅ 꼭 내용을 다시 한 번 확인해주세요!",
            ).forEach { line ->
                Text(text = line, fontSize = 13.sp, color = Gray95, modifier = Modifier.padding(bottom = 6.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "*고객이 정당하게 발급받은 쿠폰을 사장님이 임의로\n거부할 경우, 앱 이용에 제재가 있을 수 있습니다.",
                fontSize = 12.sp,
                color = Red,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "쿠폰 사용이 원활히 이루어질 수 있도록 도와주세요!", fontSize = 12.sp, color = Gray50)
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Green, RoundedCornerShape(12.dp))
                    .clickable(onClick = onConfirm),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "발급 시작하기", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(1.dp, Green, RoundedCornerShape(12.dp))
                    .clickable(onClick = onDismiss),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "취소", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Green)
            }
        }
    }
}
