package com.ece452.pillmaster

import com.ece452.pillmaster.screen.carereceiver.AutoFillEntry
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ece452.pillmaster.screen.common.CareGiverHomeScreen
import com.ece452.pillmaster.screen.common.CareReceiverHomepageScreen
import com.ece452.pillmaster.screen.common.DashboardScreen
import com.ece452.pillmaster.screen.common.HomeScreen
import com.ece452.pillmaster.screen.common.PillAddPageScreen
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.LoginViewModel

@Composable
fun PillMasterNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    context: Context
) {
    NavHost(
        navController = navController,
        startDestination = NavigationPath.DASHBOARD.route,
        modifier = modifier
    ) {
        composable(NavigationPath.DASHBOARD.route) {
            DashboardScreen(navController = navController, loginViewModel = loginViewModel)
        }
        composable(NavigationPath.HOMEPAGE.route) {
            HomeScreen(navController = navController, loginViewModel = loginViewModel, false)
        }
        composable(NavigationPath.HOMEPAGE_TEST.route) {
            HomeScreen(navController = navController, loginViewModel = loginViewModel, true)
        }
        composable(NavigationPath.CARE_RECEIVER_HOMEPAGE.route) {
            CareReceiverHomepageScreen(navController = navController)
        }
        composable(NavigationPath.CARE_GIVER_HOMEPAGE.route) {
            CareGiverHomeScreen()
        }
        composable(NavigationPath.PILL_ADD_PAGE.route) { entry ->
            PillAddPageScreen(navController = navController, entry)
        }
        composable(NavigationPath.CAMERA_HOMEPAGE.route) {

            AutoFillEntry(navController = navController, context = context)
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