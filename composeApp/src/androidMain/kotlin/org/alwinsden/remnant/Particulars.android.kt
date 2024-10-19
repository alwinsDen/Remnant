package org.alwinsden.remnant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

actual fun getPlatformName(): String {
    return "android"
}

@Composable
actual fun EntryScreen2Source(navController: NavController) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFFFFFFF)
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset {
                    IntOffset(
                        x = 90.dp.roundToPx(),
                        y = (-150).dp.roundToPx()
                    )
                },
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Why worry?\n" +
                        "What’s sad in\n" +
                        "pastures of this\n" +
                        "World?",
                textAlign = TextAlign.Right,
                fontFamily = JudsonFontFamily,
                fontSize = 30.sp
            )
            Text(
                text = "Next ->",
                color = Color(0xFF000000),
                fontFamily = InterFontFamily,
                modifier = Modifier
                    .clickable(
                        onClick = {
                            navController.navigate(NavRouteClass.Home.route)
                        },
                        interactionSource = interactionSource,
                        indication = null
                    )
            )
        }
    }
}