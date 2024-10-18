package org.alwinsden.remnant.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.alwinsden.remnant.InterFontFamily
import org.alwinsden.remnant.MatescFontFamily
import org.alwinsden.remnant.NavRouteClass
import org.jetbrains.compose.resources.painterResource
import remnant.composeapp.generated.resources.Res
import remnant.composeapp.generated.resources.kotlin_conf_25

@Composable
fun Home(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = Color(0xFF000000)
            )
    ) {
        Box(
            modifier = Modifier
                .height(85.dp)
                .width(10.dp)
        ) {

        }
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(fontFamily = MatescFontFamily, fontSize = 64.sp, color = Color(0xFFFFFFFF), text = "Remnant")
                OutlinedButton(
                    onClick = {
                        navController.navigate(NavRouteClass.EntryScreen1.route)
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFFFFF)),
                    shape = RoundedCornerShape(50),
                ) {
                    Text(
                        text = "Start your experience",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF505050),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(vertical = 5.dp, horizontal = 25.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier.padding(bottom = 25.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "created for",
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    color = Color(0xFFFFFFFF),
                    modifier = Modifier
                        .padding(
                            bottom = 5.dp
                        )
                )
                Image(
                    painter = painterResource(Res.drawable.kotlin_conf_25),
                    contentDescription = "",
                    modifier = Modifier
                        .width(220.dp)
                        .height(22.dp)
                        .background(
                            color = Color(0xFFFFFFFF)
                        ),
                    contentScale = ContentScale.FillHeight
                )
                Text(
                    text = "Multiplatform Student Contest",
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    color = Color(0xFFFFFFFF)
                )
            }
        }
    }
}