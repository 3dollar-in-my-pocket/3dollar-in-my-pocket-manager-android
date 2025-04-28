package app.threedollars.manager

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreenView()
        }
    }

}

@Preview
@Composable
fun MainScreenView() {
    val navigator: MainNavigator = rememberMainNavigator()
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
        NavigationGraph(navigator = navigator, it.calculateBottomPadding())
    }
}

@Composable
fun NavigationGraph(navigator: MainNavigator, calculateBottomPadding: Dp) {
    val context = LocalContext.current
    val navOptions: NavOptions by lazy {
        navOptions {}
    }
    NavHost(
        modifier = Modifier.padding(bottom = calculateBottomPadding),
        navController = navigator.navController,
        startDestination = navigator.startDestination,
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
            }
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
