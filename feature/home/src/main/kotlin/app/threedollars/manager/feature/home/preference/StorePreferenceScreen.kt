package app.threedollars.manager.feature.home.preference

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.threedollars.common.ui.Black
import app.threedollars.common.ui.Gray100
import app.threedollars.common.ui.Gray30
import app.threedollars.common.ui.Green
import app.threedollars.common.ui.White
import app.threedollars.manager.feature.home.R

@Composable
internal fun StorePreferenceRoute(
    onBack: () -> Unit,
    viewModel: StorePreferenceViewModel = hiltViewModel(),
) {
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    StorePreferenceScreen(
        state = uiState,
        onBack = onBack,
        onRetainLocationToggle = viewModel::toggleRetainLocationOnClose,
        onAutoOpenCloseToggle = viewModel::toggleAutoOpenCloseControl,
        onErrorDismiss = viewModel::dismissError,
    )
}

@Composable
internal fun StorePreferenceScreen(
    state: StorePreferenceState,
    onBack: () -> Unit,
    onRetainLocationToggle: () -> Unit,
    onAutoOpenCloseToggle: () -> Unit,
    onErrorDismiss: () -> Unit,
) {
    if (state.showError) {
        AlertDialog(
            onDismissRequest = onErrorDismiss,
            containerColor = White,
            text = {
                Text(
                    text = state.errorMessage ?: stringResource(id = R.string.store_preference_error_default),
                    fontSize = 16.sp,
                    color = Gray100
                )
            },
            confirmButton = {
                TextButton(onClick = onErrorDismiss) {
                    Text(text = stringResource(id = R.string.store_preference_error_confirm), color = Green)
                }
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 12.dp),
                onClick = onBack
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_home_back),
                    contentDescription = stringResource(id = R.string.store_preference_back_description)
                )
            }
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.store_preference_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Gray100,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        StorePreferenceItem(
            title = stringResource(id = R.string.store_preference_retain_location_title),
            description = stringResource(id = R.string.store_preference_retain_location_description),
            checked = state.retainLocationOnClose,
            enabled = state.isToggleEnabled,
            onToggle = onRetainLocationToggle,
        )
        Spacer(modifier = Modifier.height(24.dp))
        StorePreferenceItem(
            title = stringResource(id = R.string.store_preference_auto_open_close_title),
            description = stringResource(id = R.string.store_preference_auto_open_close_description),
            checked = state.autoOpenCloseControl,
            enabled = state.isToggleEnabled,
            onToggle = onAutoOpenCloseToggle,
        )
    }
}

@Composable
private fun StorePreferenceItem(
    title: String,
    description: String,
    checked: Boolean,
    enabled: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = { onToggle() }
            )
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Black,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = colorResource(id = R.color.gray50),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        PreferenceSwitch(checked = checked)
    }
}

@Composable
private fun PreferenceSwitch(checked: Boolean) {
    val trackColor by animateColorAsState(targetValue = if (checked) Green else Gray30, label = "trackColor")
    val thumbOffset by animateDpAsState(targetValue = if (checked) 22.dp else 2.dp, label = "thumbOffset")
    Box(
        modifier = Modifier
            .size(width = 44.dp, height = 24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(trackColor),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(20.dp)
                .clip(CircleShape)
                .background(White)
        )
    }
}

@Preview
@Composable
private fun StorePreferenceScreenPreview() {
    StorePreferenceScreen(
        state = StorePreferenceState(retainLocationOnClose = true, isLoaded = true),
        onBack = {},
        onRetainLocationToggle = {},
        onAutoOpenCloseToggle = {},
        onErrorDismiss = {},
    )
}
