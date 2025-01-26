package org.alwinsden.remnant.ui.ProductScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.alwinsden.remnant.PowerButtonPadding

@Composable
fun ProductScreen() {
    Box(modifier = Modifier.padding(top = PowerButtonPadding.dp)) {
        Text(text = "this is the same.")
    }
}