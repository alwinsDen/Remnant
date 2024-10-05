package org.alwinsden.remnant

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.alwinsden.remnant.ui.Home

@Composable
fun RoutingControl() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        //HOME: the main entry point of the application.
        composable("home") { Home() }
    }
}