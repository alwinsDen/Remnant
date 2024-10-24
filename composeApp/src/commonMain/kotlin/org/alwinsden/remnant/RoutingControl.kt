package org.alwinsden.remnant

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.alwinsden.remnant.ui.EntryScreen1
import org.alwinsden.remnant.ui.Home

@Composable
fun RoutingControl() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = NavRouteClass.Home.route, modifier = Modifier
            .background(color = Color(0xFF000000))
    ) {
        //HOME: the main entry point of the application.
        composable(
            route = NavRouteClass.Home.route,
            enterTransition = {
                scaleIntoContainer()
            },
            exitTransition = {
                scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
            },
            popEnterTransition = {
                scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
            },
            popExitTransition = {
                scaleOutOfContainer()
            }
        ) { Home(navController = navController) }
        composable(
            route = NavRouteClass.EntryScreen1.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(700)
                )
            }
        ) { EntryScreen1(navController = navController) }
        composable(route = NavRouteClass.EntryScreen2.route) { EntryScreen2Source(navController = navController) }
    }
}