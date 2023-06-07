package com.ece452.pillmaster

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ece452.pillmaster.screen.common.GiverHomeScreen
import com.ece452.pillmaster.screen.common.LoginScreen
import com.ece452.pillmaster.screen.common.OnboardScreen
import com.ece452.pillmaster.screen.common.ReceiverHomeScreen

enum class Route(val route: String) {
    ONBOARD("onboard"),
    LOGIN("login"),
    RECEIVER_HOME("receiver/home"),
    GIVER_HOME("giver/home")
    ; // TODO - Register more as needed.
}


@Composable
fun PillMasterNavHost(
    nC: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = nC,
        startDestination = Route.ONBOARD.route,
        modifier = modifier
    ) {
        composable(route = Route.ONBOARD.route) {
            OnboardScreen(
                onNav = {nC.navigateTo(Route.LOGIN.route)},
                tempReceiver = {nC.navigateTo(Route.RECEIVER_HOME.route)},
                tempGiver = {nC.navigateTo(Route.GIVER_HOME.route)},
            )
        }
        composable(route = Route.LOGIN.route) {
            LoginScreen(
                onNav = {nC.navigateTo(Route.ONBOARD.route)}
            )
        }
        composable(route = Route.RECEIVER_HOME.route) {
            ReceiverHomeScreen()
        }
        composable(route = Route.GIVER_HOME.route) {
            GiverHomeScreen()
        }
        // TODO - Register screens and specific navigation behaviours as needed.
    }
}

fun NavHostController.navigateTo(route: String) = this.navigate(route) {
    launchSingleTop = true
    restoreState = true
    popUpTo(this@navigateTo.graph.findStartDestination().id) {
        saveState = true
    }
}