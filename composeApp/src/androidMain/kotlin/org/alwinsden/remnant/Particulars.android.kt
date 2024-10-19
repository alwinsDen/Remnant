package org.alwinsden.remnant

import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.jetbrains.compose.resources.painterResource
import remnant.composeapp.generated.resources.Bell
import remnant.composeapp.generated.resources.Hydrangea
import remnant.composeapp.generated.resources.Hydrangea_bud
import remnant.composeapp.generated.resources.Res

actual fun getPlatformName(): String {
    return "android"
}

@Composable
actual fun EntryScreen2Source(navController: NavController) {
    val interactionSource = remember { MutableInteractionSource() }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFFFFFFF)
            )
    ) {
        Image(
            painter = painterResource(Res.drawable.Bell),
            contentDescription = null,
            modifier = Modifier
                .width(100.dp)
                .aspectRatio(0.9758065f)
                .align(Alignment.TopStart)
        )
        Image(
            painter = painterResource(Res.drawable.Hydrangea_bud),
            contentDescription = "",
            modifier = Modifier
                .width(200.dp)
                //this ratio is calculated from the Image resolution.
                .aspectRatio(0.87f)
                .align(Alignment.TopEnd)
        )
        Image(
            painter = painterResource(Res.drawable.Hydrangea),
            contentDescription = null,
            modifier = Modifier
                .width(100.dp)
                .aspectRatio(0.9121622f)
                .align(Alignment.BottomStart)
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 200.dp, end = 10.dp),
//                .offset {
//                    val offsetX = with(density) { (screenWidth * 0.5f).toPx() / 2 }
//                    IntOffset(
//                        x = offsetX.toInt(),
//                        y = 0
//                    )
//                },
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
                text = "next ->",
                color = Color(0xFF000000),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
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
