package org.alwinsden.remnant.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Power
import kotlinx.coroutines.runBlocking
import org.alwinsden.remnant.LocalNavController
import org.alwinsden.remnant.NavRouteClass
import org.alwinsden.remnant.dataStore.coreComponent

@Composable
fun LogoutGoogle(iconColor: Long = 0xffffffff) {
    val nvvController = LocalNavController.current
    Box() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = (-20).dp, y = 20.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Image(
                Lucide.Power,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color(iconColor)),
                modifier = Modifier
                    .clickable {
                        runBlocking {
                            coreComponent.appPreferences.addUpdateAuthKey("")
                            nvvController.navigate(NavRouteClass.Home.route)
                        }
                    }
            )
        }
    }
}