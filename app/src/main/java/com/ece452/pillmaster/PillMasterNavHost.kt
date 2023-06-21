package com.ece452.pillmaster

import com.ece452.pillmaster.screen.carereceiver.AutoFillEntry
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ece452.pillmaster.model.User
import com.ece452.pillmaster.screen.common.CareGiverHomeScreen
import com.ece452.pillmaster.screen.common.CareReceiverHomepageScreen
import com.ece452.pillmaster.screen.common.DashboardScreen
import com.ece452.pillmaster.screen.common.HomeScreen
import com.ece452.pillmaster.screen.common.LoginScreen
import com.ece452.pillmaster.screen.common.PillAddPageScreen
import com.ece452.pillmaster.screen.common.SignupScreen
import com.ece452.pillmaster.utils.NavigationPath

@Composable
fun PillMasterNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavigationPath.DASHBOARD.route,
        modifier = modifier
    ) {
        composable(NavigationPath.DASHBOARD.route) {
            DashboardScreen(navController = navController)
        }
        composable(NavigationPath.LOGIN.route) {
            LoginScreen(navController = navController)
        }
        composable(NavigationPath.SIGNUP.route) {
            SignupScreen(navController = navController)
        }
        composable("${NavigationPath.HOMEPAGE.route}/{user}",
            arguments = listOf(navArgument("user") {
                type = NavType.StringType
            }))
        { backStackEntry ->
            val userString = backStackEntry.arguments?.getString("user")
            val user = userString?.let { User.fromString(it) }
            HomeScreen(navController = navController, user = user)
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

            AutoFillEntry(navController = navController)
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