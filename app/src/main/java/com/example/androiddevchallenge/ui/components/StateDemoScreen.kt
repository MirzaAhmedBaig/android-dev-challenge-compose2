package com.example.androiddevchallenge.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.viewmodel.DemoViewModel


@Composable
fun StateDemoScreen(viewModel: DemoViewModel) {
    val name:String by viewModel.name.observeAsState("")
    Scaffold(topBar = { AppBar(appBarText = "Countdown timer") }) {
        HelloContent(name = name, onValueChange = {
            viewModel.onNameChange(it)
        })
    }

}

@Composable
fun HelloContent(name: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Hello! $name",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.h5
        )
        OutlinedTextField(
            value = name,
            onValueChange = { onValueChange(it) },
            label = { Text("Name") }
        )
    }
}