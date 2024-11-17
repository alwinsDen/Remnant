package org.alwinsden.remnant.ui.MainScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.alwinsden.remnant.PowerButtonPadding

@Composable
fun MainScreen1() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier =
                Modifier
                    .padding(top = PowerButtonPadding.dp)
        ) {
            Text(
                text = "Entry Screen 1"
            )
        }
    }
}