package org.alwinsden.remnant

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.alwinsden.remnant.dataStore.ApplicationComponent

fun main() = application {
    ApplicationComponent.init()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Remnant Desktop",
        resizable = false,
        state = WindowState(width = 1128.dp, height = 705.dp)
    ) {
        App()
    }
}