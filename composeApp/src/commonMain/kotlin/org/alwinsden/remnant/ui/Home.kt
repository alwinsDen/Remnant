package org.alwinsden.remnant.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.alwinsden.remnant.*
import org.alwinsden.remnant.components.GoogleLoginInteractible
import org.alwinsden.remnant.dataStore.coreComponent
import org.alwinsden.remnant.networking_utils.onError
import org.alwinsden.remnant.networking_utils.onSuccess
import org.jetbrains.compose.resources.painterResource
import remnant.composeapp.generated.resources.Res
import remnant.composeapp.generated.resources.kotlin_conf_25

@Composable
fun Home() {
    var isJwtVerificationLoading by remember { mutableStateOf(true) }
    val nvvController = LocalNavController.current
    LaunchedEffect(Unit) {
        val jwtTokenValue = coreComponent.appPreferences.doesAuthKeyExist()
        if (jwtTokenValue.isNotEmpty()) {
            HTTP_CALL_CLIENT.profileGetRequest()
                .onSuccess {
                    if (it.demo_completed == true) {
                        nvvController.navigate(NavRouteClass.MainScreen1.route)
                    } else {
                        nvvController.navigate(NavRouteClass.EntryScreen1.route)
                    }
                }
                .onError {
                    println("User authentication Failed.")
                    isJwtVerificationLoading = false
                }
        } else {
            isJwtVerificationLoading = false
        }
    }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
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
                Text(
                    fontFamily = MatescFontFamily,
                    fontSize = 64.sp,
                    color = Color(0xFFFFFFFF),
                    text = "Remnant"
                )
                Box(
                    modifier = Modifier
                        .height(90.dp)
                ) {
                    if (!isJwtVerificationLoading) {
                        GoogleLoginInteractible()
                    } else {
                        Row(
                            modifier =
                                Modifier
                                    .align(Alignment.Center),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xffffffff),
                                modifier = Modifier
                                    .width(18.dp)
                                    .height(18.dp),
                                strokeWidth = 2.dp
                            )
                            Text(
                                text = "Retrieving login information!",
                                color = Color(0xffffffff),
                                fontFamily = InterFontFamily,
                                fontSize = 16.sp
                            )
                        }
                    }
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