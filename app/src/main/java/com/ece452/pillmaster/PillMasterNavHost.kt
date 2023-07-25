package com.ece452.pillmaster

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.ece452.pillmaster.screen.carereceiver.AutoFillEntry
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ece452.pillmaster.screen.caregiver.CareGiverHomeScreen
import com.ece452.pillmaster.screen.caregiver.CareReceiverManageScreen
import com.ece452.pillmaster.screen.caregiver.CaregiverSettingScreen
import com.ece452.pillmaster.screen.carereceiver.CalendarScreen
import com.ece452.pillmaster.screen.carereceiver.CaregiverManageScreen
import com.ece452.pillmaster.screen.carereceiver.ChatScreen
import com.ece452.pillmaster.screen.common.ContactScreen
import com.ece452.pillmaster.screen.carereceiver.PillManageScreen
import com.ece452.pillmaster.screen.carereceiver.ReceiverSettingScreen
import com.ece452.pillmaster.screen.common.CareReceiverHomepageScreen
import com.ece452.pillmaster.screen.common.DashboardScreen
import com.ece452.pillmaster.screen.common.HomeScreen
import com.ece452.pillmaster.screen.common.LoginScreen
import com.ece452.pillmaster.screen.common.PillAddPageScreen
import com.ece452.pillmaster.screen.common.SignupScreen
import com.ece452.pillmaster.screen.common.PolicyScreen
import com.ece452.pillmaster.screen.common.UserChatScreen
import com.ece452.pillmaster.utils.NavigationPath
import com.ece452.pillmaster.viewmodel.CareGiverContactViewModel
import com.ece452.pillmaster.viewmodel.CareGiverUserChatViewModel
import com.ece452.pillmaster.viewmodel.CareReceiverContactViewModel
import com.ece452.pillmaster.viewmodel.CareReceiverUserChatViewModel

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
        composable(NavigationPath.POLICY.route) {
            PolicyScreen(navController = navController)
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
                navController = navController,
                contactViewModel = hiltViewModel<CareGiverContactViewModel>()

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
        composable(NavigationPath.CARE_GIVER_CONTACT.route) {
            ContactScreen(
                navController = navController,
                contactViewModel = hiltViewModel<CareGiverContactViewModel>()
            )
        }
        composable(NavigationPath.CARE_RECEIVER_CONTACT.route) {
            ContactScreen(
                navController = navController,
                contactViewModel = hiltViewModel<CareReceiverContactViewModel>()
            )
        }
        composable(NavigationPath.CARE_GIVER_USER_CHAT.route) { backStackEntry ->
            val receiverId = backStackEntry.arguments?.getString("receiverId") ?: ""
            val receiverEmail = backStackEntry.arguments?.getString("receiverEmail") ?: ""
            UserChatScreen(
                navController = navController,
                receiverId = receiverId,
                receiverEmail = receiverEmail,
                userChatViewModel = hiltViewModel<CareGiverUserChatViewModel>()
            )
        }
        composable(NavigationPath.CARE_RECEIVER_USER_CHAT.route) { backStackEntry ->
            val receiverId = backStackEntry.arguments?.getString("receiverId") ?: ""
            val receiverEmail = backStackEntry.arguments?.getString("receiverEmail") ?: ""
            UserChatScreen(
                navController = navController,
                receiverId = receiverId,
                receiverEmail = receiverEmail,
                userChatViewModel = hiltViewModel<CareReceiverUserChatViewModel>()
            )
        }
        composable(NavigationPath.RECEIVER_SETTING.route) {
            ReceiverSettingScreen(
                navController = navController,
            )
        }
        composable(NavigationPath.CAREGIVER_SETTING.route) {
            CaregiverSettingScreen(
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
                contactViewModel = hiltViewModel<CareReceiverContactViewModel>()

            )
        }
        composable(NavigationPath.CARERECEIVER_MANAGE.route) {
            CareReceiverManageScreen(
                navController = navController,
                contactViewModel = hiltViewModel<CareGiverContactViewModel>()

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
