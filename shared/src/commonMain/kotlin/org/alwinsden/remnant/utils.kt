package org.alwinsden.remnant

//this is the central class to control all navigation paths.

sealed class NavRouteClass(val route: String) {
    object Home : NavRouteClass("home")
    object EntryScreen1 : NavRouteClass("entryscreen_1")
    object EntryScreen2 : NavRouteClass("entryscreen_2")
}