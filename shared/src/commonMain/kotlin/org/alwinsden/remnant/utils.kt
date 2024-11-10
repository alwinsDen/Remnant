package org.alwinsden.remnant

//this is the central class to control all navigation paths.

sealed class NavRouteClass(val route: String) {
    object Home : NavRouteClass("home")

    //demo screens
    object EntryScreen1 : NavRouteClass("entryscreen_1")
    object EntryScreen2 : NavRouteClass("entryscreen_2")
    object EntryScreen3 : NavRouteClass("entryscreen_3")

    //main screens
    object MainScreen1 : NavRouteClass("mainscreen_1")
}