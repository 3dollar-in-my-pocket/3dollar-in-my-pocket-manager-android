package app.threedollars.manager.ext

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navOptions
import app.threedollars.manager.feature.ai.navigation.navigateAi
import app.threedollars.manager.feature.setting.navigation.navigateSetting
import app.threedollars.manager.navigation.factory.TabType
import app.threedollars.manager.feature.home.navigation.navigateHome
import app.threedollars.manager.feature.storemanagement.navigation.navigateStoreManagement

fun NavController.navigateTab(tab: TabType) {
    val navOptions = navOptions {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }

    }

    when (tab) {
        TabType.HOME -> this.navigateHome(navOptions)
        TabType.STORE_MANAGEMENT -> this.navigateStoreManagement(navOptions)
        TabType.AI -> this.navigateAi(navOptions)
        TabType.SETTING -> this.navigateSetting(navOptions)
    }
}