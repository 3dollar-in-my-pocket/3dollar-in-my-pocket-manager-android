package app.threedollars.manager

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import app.threedollars.common.MaintenanceStateManager
import app.threedollars.common.REVIEW_LIST
import app.threedollars.common.TabRoute
import app.threedollars.common.ui.MaintenanceDialog
import app.threedollars.common.ui.Tooltip
import app.threedollars.common.ui.TooltipTailDirection
import app.threedollars.common.ui.White
import app.threedollars.manager.ext.navigateTab
import app.threedollars.manager.feature.ai.navigation.aiNavGraph
import app.threedollars.manager.feature.home.navigation.homeNavGraph
import app.threedollars.manager.feature.review.navigation.navigateReview
import app.threedollars.manager.feature.review.navigation.reviewNavGraph
import app.threedollars.manager.feature.setting.navigation.settingNavGraph
import app.threedollars.manager.feature.storemanagement.navigation.storeManagementNavGraph
import app.threedollars.manager.navigation.MainNavigator
import app.threedollars.manager.navigation.factory.TabType
import app.threedollars.manager.navigation.rememberMainNavigator
import app.threedollars.manager.screen.BottomNavigation
import app.threedollars.manager.util.findActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val SCREEN_TYPE_KEY = "screenType"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val screenType: String? = intent?.getStringExtra(SCREEN_TYPE_KEY)
        setContent {
            MainScreenView(screenType = screenType)
        }
    }

}

@Preview
@Composable
fun MainScreenView(screenType: String? = "") {
    val navigator: MainNavigator = rememberMainNavigator()
    val showMaintenanceDialog by MaintenanceStateManager.showMaintenanceDialog.collectAsState()
    val context = LocalContext.current
    val mainViewModel: MainViewModel = androidx.hilt.navigation.compose.hiltViewModel()
    val enableSalesAIRecommendation by mainViewModel.enableSalesAIRecommendation.collectAsState()
    val showMessageTooltip by mainViewModel.showMessageTooltip.collectAsState()
    val isBottomBarVisible = navigator.shouldShowBottomBar()

    Scaffold(
        containerColor = White,
        bottomBar = {
            BottomNavigation(
                currentTab = navigator.currentTab,
                visible = isBottomBarVisible,
                enableSalesAIRecommendation = enableSalesAIRecommendation,
                onTabSelected = {
                    mainViewModel.hideMessageTooltip()
                    navigator.navController.navigateTab(it)
                }
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavigationGraph(navigator = navigator, it.calculateBottomPadding(), screenType)
            if (showMessageTooltip && isBottomBarVisible) {
                MessageTabTooltip(
                    enableSalesAIRecommendation = enableSalesAIRecommendation,
                    bottomPadding = it.calculateBottomPadding(),
                    onTimeout = mainViewModel::hideMessageTooltip,
                )
            }
        }
    }

    // 503 에러 시 점검 다이얼로그 표시
    if (showMaintenanceDialog) {
        MaintenanceDialog(
            onDismiss = {
                // 재시도: 앱 재시작을 위해 LoginActivity로 이동
                MaintenanceStateManager.reset()
                context.startActivity(Intent(context, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
                context.findActivity().finish()
            }
        )
    }
}

@Composable
private fun MessageTabTooltip(
    enableSalesAIRecommendation: Boolean,
    bottomPadding: Dp,
    onTimeout: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(MESSAGE_TAB_TOOLTIP_DURATION_MILLIS)
        onTimeout()
    }
    val tabs = TabType.entries.filter { it != TabType.AI || enableSalesAIRecommendation }
    val tabIndex = tabs.indexOf(TabType.STORE_MANAGEMENT)
    if (tabIndex < 0) return
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val itemWidth = (maxWidth - NAVIGATION_BAR_ITEM_SPACING * (tabs.size - 1)) / tabs.size
        val anchorCenterX = (itemWidth + NAVIGATION_BAR_ITEM_SPACING) * tabIndex + itemWidth / 2
        Tooltip(
            emoji = stringResource(R.string.message_main_tab_tooltip_emoji),
            message = stringResource(R.string.message_main_tab_tooltip),
            tailDirection = TooltipTailDirection.BOTTOM_CENTER,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = bottomPadding - MESSAGE_TAB_TOOLTIP_OVERLAP)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints.copy(minWidth = 0))
                    val maxX = (constraints.maxWidth - placeable.width).coerceAtLeast(0)
                    val x = (anchorCenterX.roundToPx() - placeable.width / 2).coerceIn(0, maxX)
                    layout(constraints.maxWidth, placeable.height) { placeable.place(x, 0) }
                },
        )
    }
}

private const val MESSAGE_TAB_TOOLTIP_DURATION_MILLIS = 3_000L
private val NAVIGATION_BAR_ITEM_SPACING = 8.dp
private val MESSAGE_TAB_TOOLTIP_OVERLAP = 4.dp

@Composable
fun NavigationGraph(navigator: MainNavigator, calculateBottomPadding: Dp, screenType: String? = null) {
    val context = LocalContext.current
    val navOptions: NavOptions by lazy {
        navOptions {}
    }
    NavHost(
        modifier = Modifier.padding(bottom = calculateBottomPadding),
        navController = navigator.navController,
        startDestination = when (screenType) {
            REVIEW_LIST -> TabRoute.StoreManagement::class
            else -> navigator.startDestination
        },
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        homeNavGraph(navController = navigator.navController)

        storeManagementNavGraph(
            navController = navigator.navController,
            onAllReviewNavigate = { reviewId ->
                navigator.navController.navigateReview(
                    navOptions = navOptions,
                    reviewId = reviewId
                )
            },
            screenType = screenType
        )
        aiNavGraph(

        )
        settingNavGraph(
            onMoveLoginPage = {
                context.startActivity(Intent(context, LoginActivity::class.java))
                context.findActivity().finish()
            },
        )

        reviewNavGraph(
            onStoreManagementNavigate = {
                navigator.popBackStack()
            }
        )
    }

}
