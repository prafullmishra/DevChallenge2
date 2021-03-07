package com.example.androiddevchallenge

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.almostBlack

@Composable
fun TimerUI(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .background(color = almostBlack)
            .fillMaxSize(),
    ) {
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ){
            viewModel.minutesRemaining.observeAsState().value?.let {
                Number(it, it.toProperFormat() != viewModel.timerMinutes.value)
            }
            Spacer(modifier = Modifier.width(32.dp))
            viewModel.secondsRemaining.observeAsState().value?.let {
                Number(it, it.toProperFormat() != viewModel.timerSeconds.value)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(180f)
                    .alpha(0.25f)
                    .padding(top = 48.dp, bottom = 24.dp)
                    .graphicsLayer(rotationY = 180f) //to flip horizontally
            ) {
                viewModel.minutesRemaining.observeAsState().value?.let {
                    Number(it, it.toProperFormat() != viewModel.timerMinutes.value)
                }
                Spacer(modifier = Modifier.width(32.dp))
                viewModel.secondsRemaining.observeAsState().value?.let {
                    Number(it, it.toProperFormat() != viewModel.timerSeconds.value)
                }
            }
            Box(
                Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                almostBlack,
                                almostBlack
                            )
                        )
                    )) {}
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            StopButton { viewModel.stopTimer() }
        }
    }
}

/**
 * @param number - number to show on the card
 * @param shouldAnimate - tells whether to animate showing this number. This will be false for very first value
 */
@Composable
fun Number(number: Int, shouldAnimate: Boolean){
    val numRotation = remember { Animatable(0f) }
    var numText by remember { mutableStateOf(number.toString()) }

    if(shouldAnimate) {
        LaunchedEffect(number) {
            numText = (if(number == 59) 0 else (number+1)).toProperFormat()
            numRotation.animateTo(0f, animationSpec = snap())
            numRotation.animateTo(-90f, animationSpec = tween(200, easing = LinearEasing))
            numText = (number).toProperFormat()
            numRotation.animateTo(90f, animationSpec = snap())
            numRotation.animateTo(0f, animationSpec = tween(200, easing = LinearEasing))
        }
    } else {
        numText = number.toProperFormat()
    }

    Box(
        modifier = Modifier
            .graphicsLayer(
                transformOrigin = TransformOrigin(0.5f, 0.5f),
                rotationX = numRotation.value
            )
            .background(Color.White, RoundedCornerShape(5))
            .height(110.dp)
            .width(110.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = numText, fontSize = 60.sp, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}

@Composable
fun StopButton(onStop:() -> Unit){
    Button(
        onClick = { onStop() },
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(12.dp),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 24.dp,
            pressedElevation = 4.dp
        ),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
        modifier = Modifier.size(64.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_round_stop_24),
            contentDescription = "Stop Timer",
            tint = Color.White,
            modifier = Modifier.fillMaxSize()
        )
    }
}

