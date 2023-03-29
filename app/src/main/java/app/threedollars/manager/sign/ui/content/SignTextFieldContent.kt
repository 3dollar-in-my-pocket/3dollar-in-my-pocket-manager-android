package app.threedollars.manager.sign.ui.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import app.threedollars.common.ui.Gray30
import app.threedollars.common.ui.Gray5

@Composable
fun SignTextFieldContent(
    titleText: String,
    hintText: String,
    onChangeText: (String) -> Unit,
    explanationText: String = "",
    isExplanationText: Boolean,
    keyboardType: KeyboardType,
    imeAction: ImeAction
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        SignTitleTextContent(titleText = titleText, explanationText = explanationText, isExplanationText = isExplanationText)

        TextField(
            value = text,
            onValueChange = { newText ->
                if (newText.text.length <= 20) {
                    onChangeText(newText.text)
                    text = newText
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 24.dp, end = 24.dp),
            placeholder = { Text(hintText) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                placeholderColor = Gray30,
                backgroundColor = Gray5,
                cursorColor = Gray30,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}