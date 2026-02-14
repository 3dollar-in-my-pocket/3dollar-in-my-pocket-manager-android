package app.threedollars.manager.feature.setting

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.threedollars.common.ui.DoubleBackExitHandler

@Composable
fun SettingRoute(
    onMoveLoginPage: () -> Unit,
) {
    val viewModel: SettingViewModel = hiltViewModel()

    val uiState by viewModel.stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onMoveLoginPage()
        }
    }

    when (uiState.screenType) {
        ScreenType.SETTING -> {
            DoubleBackExitHandler()

            SettingScreen(
                bossAccountInfo = uiState.bossAccountInfo,
                onClickSignOut = { viewModel.signOut() },
                onClickLogOut = { viewModel.logout() },
                onSwitchBossDevice = { isSwitch, result ->
                    if (isSwitch) {
                        viewModel.putBossDevice(result)
                    } else {
                        viewModel.deleteBossDevice()
                    }
                },
                onClickFaq = { pageType ->
                    viewModel.updateScreenType(screenType = pageType)
                }
            )
        }

        ScreenType.FAQ -> {
            BackHandler { viewModel.updateScreenType(ScreenType.SETTING) }
            FaqScreen(
                faqList = uiState.faqList,
                onClickBackButton = { pageType ->
                    viewModel.updateScreenType(screenType = pageType)
                }
            )
        }
    }
}