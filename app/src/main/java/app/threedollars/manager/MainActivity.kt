package app.threedollars.manager

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import app.threedollars.common.MaintenanceStateManager
import app.threedollars.common.REVIEW_LIST
import app.threedollars.common.TabRoute
import app.threedollars.common.ui.MaintenanceDialog
import app.threedollars.manager.ext.navigateTab
import app.threedollars.manager.feature.home.navigation.homeNavGraph
import app.threedollars.manager.feature.review.navigation.navigateReview
import app.threedollars.manager.feature.review.navigation.reviewNavGraph
import app.threedollars.manager.feature.setting.navigation.settingNavGraph
import app.threedollars.manager.feature.storemanagement.navigation.storeManagementNavGraph
import app.threedollars.manager.navigation.MainNavigator
import app.threedollars.manager.navigation.rememberMainNavigator
import app.threedollars.manager.screen.BottomNavigation
import app.threedollars.manager.util.findActivity
import dagger.hilt.android.AndroidEntryPoint


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

    Scaffold(
        bottomBar = {
            BottomNavigation(
                currentTab = navigator.currentTab,
                visible = navigator.shouldShowBottomBar(),
                onTabSelected = {
                    navigator.navController.navigateTab(it)
                }
            )
        }
    ) {
        NavigationGraph(navigator = navigator, it.calculateBottomPadding(), screenType)
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
        homeNavGraph()

        storeManagementNavGraph(
            onAllReviewNavigate = { reviewId ->
                navigator.navController.navigateReview(
                    navOptions = navOptions,
                    reviewId = reviewId
                )
            },
            screenType = screenType
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
