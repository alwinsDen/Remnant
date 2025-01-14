package org.alwinsden.remnant.ui.PopsUps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MoveUpLeft
import org.alwinsden.remnant.InterFontFamily

@Composable
fun PersonalInfoPopup(onOuterClick: () -> Unit, onSelectedOption: (selected: Int) -> Unit) {
    BottomControllerComponent(
        onOuterClick = onOuterClick,
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color(0xffffffff),
                    shape = RoundedCornerShape(
                        topStart = 25.dp,
                        topEnd = 25.dp
                    )
                )
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "Select gender: ",
                fontFamily = InterFontFamily,
                modifier = Modifier.fillMaxWidth(.9f)
            )

            //this is the selection component
            Button(
                onClick = {
                    onSelectedOption(0)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xffD9D9D9)
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(.9f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Male", fontFamily = InterFontFamily)
                    Image(
                        Lucide.MoveUpLeft,
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp),
                    )
                }
            }

            //this is the selection component
            Button(
                onClick = {
                    onSelectedOption(1)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xffD9D9D9)
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(.9f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Female", fontFamily = InterFontFamily)
                    Image(
                        Lucide.MoveUpLeft,
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp),
                    )
                }
            }

            //this is the selection component
            Button(
                onClick = {
                    onSelectedOption(2)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xffD9D9D9)
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth(.9f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("rather not say. ", fontFamily = InterFontFamily)
                    Image(
                        Lucide.MoveUpLeft,
                        contentDescription = null,
                        modifier = Modifier
                            .width(20.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun BottomControllerComponent(onOuterClick: () -> Unit, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(
                color = Color(0xff2B2B2B).copy(alpha = .57f)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onOuterClick()
            },
        verticalArrangement = Arrangement.Bottom
    ) {
        content()
    }
}