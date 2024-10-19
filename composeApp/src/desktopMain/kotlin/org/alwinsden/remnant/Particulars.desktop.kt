package org.alwinsden.remnant

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

actual fun getPlatformName(): String {
    return "desktop"
}

@Composable
actual fun EntryScreen2Source(navController: NavController) {
    Text(text = "desktop expect function")
}
