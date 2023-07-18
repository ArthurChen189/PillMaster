package com.ece452.pillmaster

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.ece452.pillmaster.screen.carereceiver.AutoFillEntry
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ece452.pillmaster.screen.caregiver.CareGiverMessageScreen
import com.ece452.pillmaster.screen.carereceiver.CalendarScreen
import com.ece452.pillmaster.screen.carereceiver.CaregiverManageScreen
import com.ece452.pillmaster.screen.carereceiver.ChatScreen
import com.ece452.pillmaster.screen.carereceiver.MessageScreen
import com.ece452.pillmaster.screen.carereceiver.PillManageScreen
import com.ece452.pillmaster.screen.carereceiver.ReceiverSettingScreen
import com.ece452.pillmaster.screen.common.CareGiverHomeScreen
import com.ece452.pillmaster.screen.common.CareReceiverHomepageScreen
import com.ece452.pillmaster.screen.common.DashboardScreen
import com.ece452.pillmaster.screen.common.HomeScreen
import com.ece452.pillmaster.screen.common.LoginScreen
import com.ece452.pillmaster.screen.common.PillAddPageScreen
import com.ece452.pillmaster.screen.common.SignupScreen
import com.ece452.pillmaster.utils.NavigationPath

@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.O)
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
            val userId = backStackEntry.arguments?.getString("user")
            HomeScreen(
                navController = navController,
                userId = userId,
            )
        }
        composable(NavigationPath.CARE_RECEIVER_HOMEPAGE.route) {
            CareReceiverHomepageScreen(
                navController = navController,
            )
        }
        composable(NavigationPath.CARE_GIVER_HOMEPAGE.route) {
            CareGiverHomeScreen(
                navController = navController
            )
        }
        composable(NavigationPath.CARE_GIVER_MESSAGE.route) {
            CareGiverMessageScreen(
                navController = navController
            )
        }
        composable(NavigationPath.PILL_ADD_PAGE.route) { entry ->
            PillAddPageScreen(navController = navController, entry)
        }
        composable(NavigationPath.CAMERA_HOMEPAGE.route) {

            AutoFillEntry(navController = navController)
        }
        composable(NavigationPath.CALENDAR.route) {
            CalendarScreen(
                navController = navController,
            )
        }
        composable(NavigationPath.MESSAGE.route) {
            MessageScreen(
                navController = navController,
            )
        }
        composable(NavigationPath.RECEIVER_SETTING.route) {
            ReceiverSettingScreen(
                navController = navController,
            )
        }
        composable(NavigationPath.PILL_MANAGE.route) {
            PillManageScreen(
                navController = navController,
            )
        }
        composable(NavigationPath.CAREGIVER_MANAGE.route) {
            CaregiverManageScreen(
                navController = navController,
            )
        }

        composable(NavigationPath.HEALTH_BOT_PATH.route) {
            ChatScreen(
                navController = navController,
            )
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
