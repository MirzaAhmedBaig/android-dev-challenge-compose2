package com.example.androiddevchallenge.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.androiddevchallenge.R

@Composable
fun AppBar(appBarText: String) {
    TopAppBar(
        title = { Text(text = appBarText) },
        navigationIcon = {
            IconButton(
                onClick = {
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_timer),
                    contentDescription = "timer_icon"
                )
            }
        }
    )

}