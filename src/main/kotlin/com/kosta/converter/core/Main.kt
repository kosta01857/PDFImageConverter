package com.kosta.converter.core

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.kosta.converter.screens.ImageToPDFConverterApp
import com.kosta.converter.screens.ImageToPDFConverterApp2

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        resizable = false,
        title = "PDFConverter",
        state = rememberWindowState(width = 600.dp, height = 400.dp)
    ) {
        ImageToPDFConverterApp2()
    }
}
