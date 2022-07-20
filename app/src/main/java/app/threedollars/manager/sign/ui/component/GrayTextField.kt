package app.threedollars.manager.sign.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.threedollars.common.ui.Gray30
import app.threedollars.common.ui.Gray5
import app.threedollars.common.ui.Pink

@Composable
fun GrayTextField(
    text: Int,
    onTextChange: (String) -> Unit,
    title: Int = 0,
    hint: Int = 0,
    description: Int = 0,
    maxLine: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
) {
    GrayTextField(
        stringResource(text),
        onTextChange,
        stringResource(id = title),
        stringResource(id = hint),
        stringResource(id = description),
        maxLine,
        keyboardOptions
    )
}

@Composable
fun GrayTextField(
    text: String,
    onTextChange: (String) -> Unit,
    title: String = "",
    hint: String = "",
    description: String = "",
    maxLine: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        TitleLabel(title, description)
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = text,
            maxLines = maxLine,
            shape = RoundedCornerShape(8.dp),
            onValueChange = onTextChange,
            label = null,
            textStyle = TextStyle(Black),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Gray5,
                cursorColor = DarkGray,
                disabledLabelColor = Gray30,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = { Text(text = hint, color = Gray30) },
        )
    }
}

@Composable
fun TitleLabel(title: String, description: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 8.dp)
    ) {
        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.CenterStart),
            textAlign = TextAlign.Start,
            color = Black,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = description,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = Pink,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}