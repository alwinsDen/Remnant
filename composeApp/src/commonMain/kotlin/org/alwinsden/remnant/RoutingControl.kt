package org.alwinsden.remnant

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.alwinsden.remnant.components.LogoutGoogle
import org.alwinsden.remnant.controlThemes.BackgroundTheme
import org.alwinsden.remnant.ui.DemoScreens.EntryScreen1
import org.alwinsden.remnant.ui.DemoScreens.EntryScreen3
import org.alwinsden.remnant.ui.Home
import org.alwinsden.remnant.ui.MainScreens.MainScreen1


@Composable
fun RoutingControl() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = NavRouteClass.Home.route,
            modifier = Modifier
                .background(color = Color(0xff000000))
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
            ) {
                BackgroundTheme(color = 0xff000000) {
                    Home()
                }
            }
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
            ) {
                BackgroundTheme(color = 0xff000000) {
                    EntryScreen1()
                    LogoutGoogle(iconColor = 0xffffffff)
                }
            }
            composable(route = NavRouteClass.EntryScreen2.route) {
                BackgroundTheme {
                    EntryScreen2Source()
                    LogoutGoogle(iconColor = 0xff000000)
                }
            }
            composable(route = NavRouteClass.EntryScreen3.route) {
                BackgroundTheme(color = 0xffffffff) {
                    EntryScreen3()
                    LogoutGoogle(iconColor = 0xff000000)
                }
            }
            composable(route = NavRouteClass.MainScreen1.route) {
                BackgroundTheme(color = 0xffffffff) {
                    LogoutGoogle(iconColor = 0xff000000)
                    MainScreen1()
                }
            }
        }
    }
}