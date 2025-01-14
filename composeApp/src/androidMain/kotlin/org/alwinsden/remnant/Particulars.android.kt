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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.alwinsden.remnant.ui.MainScreens.SpecialColoredBckBox
import org.jetbrains.compose.resources.painterResource
import remnant.composeapp.generated.resources.*

actual fun getPlatformName(): String {
    return "android"
}

@Composable
actual fun EntryScreen2Source() {
    val interactionSource = remember { MutableInteractionSource() }
    val nvvController = LocalNavController.current
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFFFFFFF)
            )
    ) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        Image(
            painter = painterResource(Res.drawable.Bell),
            contentDescription = null,
            modifier = Modifier
                .width(screenWidth * 0.25f)
                .aspectRatio(0.9758065f)
                .align(Alignment.TopStart)
        )
        Image(
            painter = painterResource(Res.drawable.Hydrangea_bud),
            contentDescription = "",
            modifier = Modifier
                .width(screenWidth * 0.5f)
                //this ratio is calculated from the Image resolution.
                .aspectRatio(0.87f)
                .align(Alignment.TopEnd)
        )
        Image(
            painter = painterResource(Res.drawable.Hydrangea),
            contentDescription = null,
            modifier = Modifier
                .width(screenWidth * 0.25f)
                .aspectRatio(0.9121622f)
                .align(Alignment.BottomStart)
        )
        Image(
            painter = painterResource(Res.drawable.Osteospermum_3),
            contentDescription = null,
            modifier = Modifier
                .width(screenWidth * 0.4f)
                .aspectRatio(0.8307692f)
                .align(Alignment.BottomEnd)
        )
        Image(
            painter = painterResource(Res.drawable.Tulip),
            contentDescription = null,
            modifier = Modifier
                .width(screenWidth * 0.7f)
                .aspectRatio(0.7219917f)
                .align(Alignment.BottomCenter)
        )
        Image(
            painter = painterResource(Res.drawable.Romashka),
            contentDescription = null,
            modifier = Modifier
                .width(screenWidth * 0.45f)
                .aspectRatio(0.5f)
                .align(Alignment.CenterStart)
                .offset(x = 0.dp, y = -(screenHeight / 2) * 0.4f)
        )
        Image(
            painter = painterResource(Res.drawable.Hydrangea_bud_1),
            contentDescription = null,
            modifier = Modifier
                .width(screenWidth * 0.45f)
                .aspectRatio(0.4592902f)
                .align(Alignment.CenterStart)
                .offset(x = 0.dp, y = (screenHeight / 2) * 0.4f)
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 220.dp, end = 10.dp),
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
                fontSize = 35.sp
            )
            Text(
                text = "next ->",
                color = Color(0xFF000000),
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                modifier = Modifier
                    .clickable(
                        onClick = {
                            nvvController.navigate(NavRouteClass.EntryScreen3.route)
                        },
                        interactionSource = interactionSource,
                        indication = null
                    )
            )
        }
    }
}

actual val PowerButtonPadding = 40;
actual val mainScreenColumnWidth: Dp = Dp.Unspecified

@Preview
@Composable
fun test() {
    SpecialColoredBckBox(
        bckColor = 0xff000000,
        header = "Things we should about you?",
        content = "Things we should about you? Things we should about you? Things we should about you? Things we should about you? Things we should about you? Things we should about you? Things we should about you? Things we should about you? Things we should about you? "
    )
}