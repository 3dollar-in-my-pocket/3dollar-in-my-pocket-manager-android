package app.threedollars.manager.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import app.threedollars.common.TabRoute
import app.threedollars.manager.feature.home.HomeRoute
import app.threedollars.manager.feature.home.preference.StorePreferenceRoute as StorePreferenceScreenRoute
import kotlinx.serialization.Serializable

@Serializable
internal data class StorePreferenceRoute(val storeId: String)

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(
        route = TabRoute.Home,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.homeNavGraph(
    navController: NavController,
) {
    composable<TabRoute.Home> {
        HomeRoute(
            onPreferenceNavigate = { storeId ->
                navController.navigate(StorePreferenceRoute(storeId = storeId)) {
                    launchSingleTop = true
                }
            }
        )
    }
    composable<StorePreferenceRoute> {
        StorePreferenceScreenRoute(
            onBack = { navController.popBackStack() }
        )
    }
}
