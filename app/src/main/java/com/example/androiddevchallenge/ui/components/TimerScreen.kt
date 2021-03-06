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
package com.example.androiddevchallenge.ui.components

import android.os.CountDownTimer
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.extensions.formatElapsedTime
import com.example.androiddevchallenge.viewmodel.TimerViewModel

@Composable
fun TimerScreen(viewModel: TimerViewModel) {

    var isRunning by rememberSaveable { mutableStateOf(false) }
    val time by viewModel.millisUntilFinished.observeAsState()

    DisposableEffect(isRunning) {
        val countdownTimer =
            object : CountDownTimer(
                if (time == 0L) viewModel.millisInFuture else time!!,
                viewModel.countdownInterval
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    viewModel.onTimeChanged(millisUntilFinished)
                }

                override fun onFinish() {
                    isRunning = false
                    viewModel.onTimeChanged(viewModel.millisInFuture)
                }
            }

        if (isRunning) {
            countdownTimer.start()
        } else {
            countdownTimer.cancel()
        }

        onDispose {
            countdownTimer.cancel()
        }
    }

    val progress = time!! / viewModel.millisInFuture.toFloat()

    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            val target = if (progress == 0f) 1f else progress
            val result = animatedProgress.animateTo(target)
            if (result.endReason == AnimationEndReason.Finished) {
                val nextDurationInMillis = time!!.toInt()
                animatedProgress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = nextDurationInMillis,
                        easing = LinearEasing
                    )
                )
            }
        } else {
            animatedProgress.animateTo(progress)
        }
    }

    Scaffold(topBar = { AppBar(appBarText = "Countdown Timer") }) {
        TimerScreenContent(
            time = time!!,
            progress = animatedProgress.value,
            isRunning = isRunning,
            onPlay = {
                isRunning = true
            },
            onPause = {
                isRunning = false
            },
            onReset = {
                isRunning = false
                viewModel.onTimeChanged(viewModel.millisInFuture)
            }
        )
    }
}

@Composable
fun TimerScreenContent(
    time: Long,
    progress: Float,
    isRunning: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(.2f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 0.dp, start = 64.dp, end = 64.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                color = MaterialTheme.colors.primary,
                strokeWidth = 5.dp,
                progress = progress
            )
            Text(
                text = time.formatElapsedTime(),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h2.copy(fontWeight = FontWeight.Normal)
            )
            TextButton(onClick = onReset, modifier = Modifier.padding(top = 100.dp)) {
                Text(stringResource(R.string.countdown_timer_reset_button))
            }
        }
        Spacer(modifier = Modifier.weight(.3f))
        TimerButton(isRunning, onPlay, onPause)
        Spacer(modifier = Modifier.weight(.1f))
    }
}
