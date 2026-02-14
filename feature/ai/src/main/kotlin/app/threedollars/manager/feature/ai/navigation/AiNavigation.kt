package app.threedollars.manager.feature.ai.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import app.threedollars.common.TabRoute
import app.threedollars.manager.feature.ai.AiRoute

fun NavController.navigateAi(navOptions: NavOptions) {
    navigate(
        route = TabRoute.AI,
        navOptions = navOptions
    )
}

fun NavGraphBuilder.aiNavGraph() {
    composable<TabRoute.AI> {
        AiRoute()
    }
}
