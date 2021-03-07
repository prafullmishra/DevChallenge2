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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    @ExperimentalAnimationApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                val hasTimerStarted: Boolean by viewModel.hasTimerStarted.observeAsState(false)
                MyApp(
                    viewModel = viewModel,
                    hasTimerStarted = hasTimerStarted,
                    onBackPressed = { onBackPressed() })
            }
        }
    }

    /**
     * Not the best way to handle navigation :)
     */
    override fun onBackPressed() {
        if(viewModel.onBackPressed()) {
            super.onBackPressed()
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun MyApp(viewModel: MainViewModel, hasTimerStarted: Boolean, onBackPressed:() -> Unit) {
    when {
        hasTimerStarted -> TimerUI(viewModel = viewModel)
        else -> InputTime(viewModel = viewModel)
    }
}
