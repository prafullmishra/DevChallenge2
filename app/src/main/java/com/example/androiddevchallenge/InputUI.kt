/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.almostBlack
import com.example.androiddevchallenge.ui.theme.teal
import java.util.Locale

@ExperimentalAnimationApi
@Composable
fun InputTime(viewModel: MainViewModel) {

    val inputMinutes = viewModel.timerMinutes.observeAsState("00")
    val inputSeconds = viewModel.timerSeconds.observeAsState("00")

    Column(
        Modifier
            .fillMaxSize()
            .background(almostBlack)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .height(120.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.Bottom) {
                    IncrementButton(onClick = { viewModel.incrementMinutes() })
                    NumeralUnit("m", 0f) // just a placeholder for aligning button wrt digit only
                }

                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Numeral(inputMinutes.value)
                    NumeralUnit("m")
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.Bottom,
                ) {
                    DecrementButton(onClick = { viewModel.decrementMinutes() })
                    NumeralUnit("m", 0f)
                }
            }

            Spacer(Modifier.width(56.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.Bottom) {
                    IncrementButton(onClick = { viewModel.incrementSeconds() })
                    NumeralUnit("s", 0f) // just a placeholder for aligning button wrt. digit only
                }

                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Numeral(inputSeconds.value)
                    NumeralUnit("s")
                }
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    DecrementButton(onClick = { viewModel.decrementSeconds() })
                    NumeralUnit("s", 0f)
                }
            }
        }
        AnimatedVisibility(
            visible = (inputMinutes.value != "00" || inputSeconds.value != "00"),
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                StartButton { viewModel.startTimer() }
            }
        }
    }
}

@Composable
fun Numeral(text: String) {
    Text(
        text = text,
        fontFamily = FontFamily.SansSerif,
        color = Color.LightGray,
        fontSize = 80.sp,
    )
}

@Composable
fun NumeralUnit(text: String, alpha: Float = 1f) {
    Text(
        text = text.toLowerCase(Locale.ROOT),
        fontFamily = FontFamily.SansSerif,
        color = Color.Gray,
        fontSize = 20.sp,
        modifier = Modifier
            .alpha(alpha)
            .padding(bottom = 16.dp)
    )
}

@Composable
fun IncrementButton(onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(12.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 24.dp,
            pressedElevation = 4.dp
        ),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
        modifier = Modifier.size(48.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_add_24),
            contentDescription = "Increment Minute",
            tint = Color.Black,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun DecrementButton(onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(12.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 24.dp,
            pressedElevation = 4.dp
        ),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray),
        modifier = Modifier.size(48.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_remove_24),
            contentDescription = "Decrement Minute",
            tint = Color.Black,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun StartButton(onStart: () -> Unit) {
    Button(
        onClick = { onStart() },
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(12.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 24.dp,
            pressedElevation = 4.dp
        ),
        colors = ButtonDefaults.buttonColors(backgroundColor = teal),
        modifier = Modifier.size(64.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_play_arrow_24),
            contentDescription = "Start Timer",
            tint = almostBlack,
            modifier = Modifier.fillMaxSize()
        )
    }
}
