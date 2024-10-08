package org.alwinsden.remnant

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.alwinsden.remnant.ui.EntryScreen1
import org.alwinsden.remnant.ui.EntryScreen2
import org.alwinsden.remnant.ui.Home

@Composable
fun RoutingControl() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavRouteClass.Home.route) {
        //HOME: the main entry point of the application.
        composable(NavRouteClass.Home.route) { Home(navController = navController) }
        composable(NavRouteClass.EntryScreen1.route) { EntryScreen1(navController = navController) }
        composable(NavRouteClass.EntryScreen2.route) { EntryScreen2(navController = navController) }
    }
}