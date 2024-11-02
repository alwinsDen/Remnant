package org.alwinsden.remnant

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.alwinsden.remnant.dataStore.ApplicationComponent

fun main() = application {
    ApplicationComponent.init()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Remnant",
    ) {
        App()
    }
}