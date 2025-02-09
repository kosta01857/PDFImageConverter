package com.kosta.converter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kosta.converter.viewmodel.MainViewModel


@Composable
fun ImageToPDFConverterApp2() {
    val viewModel = remember { MainViewModel() } // Simple manual creation
    val uiState by viewModel.viewModelState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Drag and drop an image here:", style = MaterialTheme.typography.h6)
        // Drag and drop box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(2.dp, if (uiState.isDragging) Color.Blue else Color.Gray)
                .background(if (uiState.isDragging) Color.LightGray else Color.Transparent)

        ) {
            TODO()
        }

        Spacer(modifier = Modifier.padding(16.dp))

        // Show the selected image path, if it exists
        uiState.imagePath?.let {
            Text("selected image $it")
        }

    }
}