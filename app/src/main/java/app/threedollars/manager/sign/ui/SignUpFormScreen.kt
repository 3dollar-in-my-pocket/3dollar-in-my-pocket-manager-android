package app.threedollars.manager.sign.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.threedollars.manager.R
import app.threedollars.manager.sign.ui.component.*
import app.threedollars.manager.sign.viewmodel.LoginViewModel


@Preview
@Composable
fun SignUpFormScreen(selectImage: LoginViewModel) {
    var userName by remember {
        mutableStateOf("")
    }
    var storeName by remember {
        mutableStateOf("")
    }
    var storeNumber by remember {
        mutableStateOf("")
    }
    var telNumber by remember {
        mutableStateOf("")
    }
    var image by remember {
        mutableStateOf("")
    }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        TitleWithBackButton(backButtonClicked = { /*TODO*/ }, stringResId = R.string.join)
        GrayTextField(
            text = userName,
            onTextChange = { text: String -> userName = text },
            title = stringResource(id = R.string.manager_name),
            hint = stringResource(id = R.string.manager_name_hint),
        )
        GrayTextField(
            text = userName,
            onTextChange = { text: String -> userName = text },
            title = stringResource(id = R.string.manager_name),
            hint = stringResource(id = R.string.manager_name_hint),
        )
        GrayTextField(
            text = storeName,
            onTextChange = { text: String ->
                storeName = if (text.length > 20) text.take(20) else text
            },
            title = stringResource(id = R.string.store_name),
            hint = stringResource(id = R.string.store_name_hint),
            description = stringResource(id = R.string.store_name_max)
        )
        GrayTextField(
            text = storeNumber,
            onTextChange = { text: String -> storeNumber = text },
            title = stringResource(id = R.string.store_number),
            hint = stringResource(id = R.string.store_number_hint),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            description = stringResource(id = R.string.input_without_dash)
        )
        GrayTextField(
            text = telNumber,
            onTextChange = { text: String -> telNumber = text },
            title = stringResource(id = R.string.phone_number),
            hint = stringResource(id = R.string.phone_number_hint),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            description = stringResource(id = R.string.input_without_dash)
        )
        Row(Modifier.padding(horizontal = 24.dp)) {
            TitleLabel(
                title = stringResource(id = R.string.select_category),
                description = stringResource(id = R.string.max_three)
            )
        }
        Column(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                )
                .fillMaxWidth()
        ) {
            CategoryTextButtonGroup(
                tags = listOf(
                    Category(
                        0, true, "한식", "622b7d0105ecea5baeafd245"
                    ),
                    Category(
                        0, true, "양식", "622b7d0105ecea5baeafd246"
                    ),
                )
            )
        }
        Row(Modifier.padding(horizontal = 24.dp)) {
            TitleLabel(
                title = stringResource(id = R.string.store_photo),
                description = stringResource(id = R.string.store_photo_desc)
            )
        }
        Image(
            painterResource(R.drawable.ic_kakao),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .height(160.dp)
        )
        Row(Modifier.padding(horizontal = 24.dp)) {
            BasicButton(text = stringResource(id = R.string.upload_image)) {
                // selectImage()
            }
        }
        Spacer(modifier = Modifier.padding(24.dp))
    }
}

@Composable
fun TitleWithBackButton(
    backButtonClicked: () -> Unit,
    @StringRes stringResId: Int
) {
    Column(
        Modifier
            .padding(horizontal = 30.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier.fillMaxWidth()
        ) {
            /*Button(
                onClick = { backButtonClicked() },
                modifier = Modifier
                    .background(Color.White)
                    .padding(0.dp),
            ) {
                painterResource(id = R.drawable.ic_back)
            }*/
            Text(
                text = stringResource(id = stringResId),
                style = TextStyle(color = Color.Black),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp)
            )
        }
    }
}