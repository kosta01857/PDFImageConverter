package com.kosta.converter.core

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kosta.converter.ui.ImageToPDFConverterApp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Image to PDF Converter",
        state = rememberWindowState(width = 800.dp, height = 600.dp), // Set initial size
        resizable = false
    ) {
        ImageToPDFConverterApp()
    }
}
