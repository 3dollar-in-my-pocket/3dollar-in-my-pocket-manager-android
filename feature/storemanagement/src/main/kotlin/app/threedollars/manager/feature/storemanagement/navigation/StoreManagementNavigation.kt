package app.threedollars.manager.feature.storemanagement.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import app.threedollars.common.TabRoute
import app.threedollars.manager.feature.storemanagement.StoreManagementRoute
import app.threedollars.manager.feature.storemanagement.components.storepost.StorePostViewModel
import app.threedollars.manager.feature.storemanagement.components.storepost.UploadPostRoute
import kotlinx.serialization.Serializable

@Serializable
internal data object UploadStorePostRoute

fun NavController.navigateStoreManagement(
    navOptions: NavOptions,
) {
    navigate(
        route = TabRoute.StoreManagement,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.storeManagementNavGraph(
    navController: NavController,
    onAllReviewNavigate: (String?) -> Unit,
    screenType: String?
) {
    composable<TabRoute.StoreManagement> {
        StoreManagementRoute(
            onAllReviewNavigate = onAllReviewNavigate,
            onUploadPostNavigate = { navController.navigate(UploadStorePostRoute) },
            screenType = screenType
        )
    }
    composable<UploadStorePostRoute> { entry ->
        val storeManagementEntry = remember(entry) { navController.getBackStackEntry<TabRoute.StoreManagement>() }
        val viewModel: StorePostViewModel = hiltViewModel(storeManagementEntry)
        UploadPostRoute(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
        )
    }
}
