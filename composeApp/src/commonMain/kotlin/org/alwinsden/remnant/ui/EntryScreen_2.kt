package org.alwinsden.remnant.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.alwinsden.remnant.InterFontFamily
import org.alwinsden.remnant.JudsonFontFamily
import org.alwinsden.remnant.NavRouteClass
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun EntryScreen2(navController: NavController) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFFFFFFF)
            )
    ) {
        Column {
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
