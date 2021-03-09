package com.example.androiddevchallenge.ui.components

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
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
    val time by viewModel.millisUntilFinished.observeAsState(viewModel.millisInFuture)

    DisposableEffect(time==viewModel.millisInFuture) {
        val countdownTimer =
            object : CountDownTimer(time, viewModel.countdownInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    viewModel.onTimeChanged(millisUntilFinished)
                }

                override fun onFinish() {
                    isRunning = false
                }
            }

        if (isRunning) {
            countdownTimer.start()
        } else {
            countdownTimer.cancel()
        }
        Log.d("TimerScreen", " Calling to DisposableEffect $time")

        onDispose {
            countdownTimer.cancel()
        }
    }

    val progress = if (isRunning) time / viewModel.millisInFuture.toFloat() else 0f

    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            val target = if (progress == 0f) 1f else progress
            // Start the first animation using spring and wait for result
            val result = animatedProgress.animateTo(target)
            if (result.endReason == AnimationEndReason.Finished) {
                val nextDurationInMillis = (viewModel.millisInFuture).toInt()
                // Now we can start the second one using tween
                animatedProgress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = nextDurationInMillis,
                        easing = LinearEasing
                    )
                )
            }
        } else {
            if (progress == 1f)
                animatedProgress.animateTo(1f)
        }
    }


    Scaffold(topBar = { AppBar(appBarText = "Countdown Timer") }) {
        TimerScreenContent(
            time = time.formatElapsedTime(),
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
    time: String,
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
                text = time,
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
