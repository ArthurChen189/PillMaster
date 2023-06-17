package com.ece452.pillmaster

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ece452.pillmaster.screen.carereceiver.CalendarScreen
import com.ece452.pillmaster.screen.common.CareGiverHomeScreen
import com.ece452.pillmaster.screen.common.CareReceiverHomepageScreen
import com.ece452.pillmaster.screen.common.DashboardScreen
import com.ece452.pillmaster.screen.common.HomeScreen
import com.ece452.pillmaster.screen.common.PillAddPageScreen
import com.ece452.pillmaster.utils.NavigationPath

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PillMasterNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = NavigationPath.DASHBOARD.route,
        modifier = modifier
    ) {
        composable(NavigationPath.DASHBOARD.route) {
            DashboardScreen(navController = navController)
        }
        composable(NavigationPath.HOMEPAGE.route) {
            HomeScreen(navController = navController,false)
        }
        composable(NavigationPath.HOMEPAGE_TEST.route) {
            HomeScreen(navController = navController, true)
        }
        composable(NavigationPath.CARE_RECEIVER_HOMEPAGE.route) {
            CareReceiverHomepageScreen(navController = navController)
        }
        composable(NavigationPath.CARE_GIVER_HOMEPAGE.route) {
            CareGiverHomeScreen()
        }
        composable(NavigationPath.PILL_ADD_PAGE.route) {
            PillAddPageScreen(navController = navController)
        }
        composable(NavigationPath.CALENDAR.route) {
            CalendarScreen(navController = navController)
        }
    }
}

fun NavHostController.navigateTo(route: String) = this.navigate(route) {
    launchSingleTop = true
    restoreState = true
    popUpTo(this@navigateTo.graph.findStartDestination().id) {
        saveState = true
    }
}