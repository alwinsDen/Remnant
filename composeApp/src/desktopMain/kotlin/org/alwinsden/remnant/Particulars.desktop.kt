package org.alwinsden.remnant

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

actual fun getPlatformName(): String {
    return "desktop"
}

@Composable
actual fun EntryScreen2Source() {
    Text(text = "desktop expect function")
}

data class Progress(
    val fraction: Float,
    val timeMillis: Long
)
