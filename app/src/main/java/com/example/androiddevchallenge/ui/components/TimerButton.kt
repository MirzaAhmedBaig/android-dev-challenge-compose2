package com.example.androiddevchallenge.ui.components

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.androiddevchallenge.R

@Composable
fun TimerButton(
    isRunning: Boolean,
    onPlay: () -> Unit,
    onPause: () -> Unit
) {
    FloatingActionButton(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.background,
        onClick = {
            if (isRunning) {
                onPause()
            } else {
                onPlay()
            }
        }
    ) {
        if (isRunning) {
            Icon(
                imageVector = Icons.Rounded.Pause,
                contentDescription = stringResource(R.string.countdown_timer_stop_button)
            )
        } else {
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = stringResource(R.string.countdown_timer_start_button)
            )
        }
    }
}