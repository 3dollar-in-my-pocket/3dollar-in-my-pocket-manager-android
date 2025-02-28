package app.threedollars.manager.feature.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import app.threedollars.common.Route
import app.threedollars.manager.feature.review.ReviewRoute

fun NavController.navigateReview(
    navOptions: NavOptions,
) {
    navigate(
        route = Route.Review,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.reviewNavGraph(
    onStoreManagementNavigate: () -> Unit
) {
    composable<Route.Review> {
        ReviewRoute(
            onStoreManagementNavigate = onStoreManagementNavigate
        )
    }
}
