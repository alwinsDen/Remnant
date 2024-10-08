package org.alwinsden.remnant.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun EntryScreen2(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFFFFFFF)
            )
    ) {
        Text(
            text = "Test test"
        )
    }
}