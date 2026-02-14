package app.threedollars.manager.feature.ai

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AiRoute() {
    val viewModel: AiViewModel = hiltViewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    AiScreen(
        uiState = uiState,
        onRetry = { viewModel.retry() }
    )
}
