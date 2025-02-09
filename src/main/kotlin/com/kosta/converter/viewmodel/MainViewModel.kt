package com.kosta.converter.viewmodel

import androidx.lifecycle.ViewModel
import com.kosta.converter.core.enums.ImageFormat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MainWindowState(
    val imagePath: String? = null,
    val outputDirectory: String? = null,
    val isDragging: Boolean = false,
    val statusMessage: String = "",
    val targetFormat: ImageFormat = ImageFormat.PDF

    )

class MainViewModel: ViewModel() {
    private val internalViewModelState = MutableStateFlow(MainWindowState())
    val viewModelState = internalViewModelState.asStateFlow()

    private fun updateState(update: MainWindowState.() -> MainWindowState){
        internalViewModelState.update(update)
    }

    fun updateImagePath(newImagePath: String) = updateState { copy(imagePath = newImagePath) }
    fun updateOutputDirectory(newDirectoryPath: String?) = updateState { copy(outputDirectory = newDirectoryPath) }
    fun updateIsDragging(isDragging: Boolean) = updateState { copy(isDragging = isDragging) }
    fun updateStatusMessage(newStatusMessage: String) = updateState { copy(statusMessage = newStatusMessage) }
    fun updateTargetFormat(newStatusMessage: ImageFormat) = updateState { copy(targetFormat = targetFormat) }
}