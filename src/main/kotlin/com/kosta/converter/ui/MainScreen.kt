package com.kosta.converter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kosta.converter.core.enums.ImageFormat
import com.kosta.converter.domain.usecase.ImageConverterUseCase
import com.kosta.converter.viewmodel.MainViewModel
import com.kosta.converter.viewmodel.MainWindowState
import java.awt.Desktop
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.*
import java.io.File
import javax.swing.JFileChooser
import javax.swing.UIManager

val validExtensions = listOf("png", "jpg", "jpeg")


@Composable
fun ImageToPDFConverterApp() {
    val viewModel = remember { MainViewModel() } // Simple manual creation
    val uiState by viewModel.viewModelState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Drag and drop an image here:", style = MaterialTheme.typography.h6)

        Row {
            DragAndDropBox(uiState, viewModel)
            Spacer(modifier = Modifier.width(16.dp))
            ChooseTargetFormat(viewModel)
        }

        Spacer(modifier = Modifier.padding(16.dp))

        ShowImageNameIfSelected(uiState)

        OutputDirectorySelection(uiState, viewModel)

        Spacer(modifier = Modifier.padding(16.dp))

        ConvertToPDF(uiState, viewModel)

        // status message
        Text(uiState.statusMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))

        Spacer(modifier = Modifier.padding(16.dp))

        uiState.outputDirectory?.let {
            OpenOutputFolder(it)
        }

    }
}

@Composable
fun DragAndDropBox(uiState: MainWindowState, viewModel: MainViewModel) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .border(2.dp, if (uiState.isDragging) Color.Blue else Color.Gray)
            .background(if (uiState.isDragging) Color.LightGray else Color.Transparent)

    ) {
        DragDropTarget(
            onDragExit = { viewModel.updateIsDragging(false) },
            onDragEnter = { viewModel.updateIsDragging(true) },
        ) {
            if (it.extension !in validExtensions) {
                viewModel.updateStatusMessage("Invalid file type! Please drop an image")
            } else {
                viewModel.updateImagePath(it.absolutePath)
                viewModel.updateStatusMessage("Image loaded successfully")
            }
        }
    }
}

@Composable
fun ShowImageNameIfSelected(uiState: MainWindowState) {
    uiState.imagePath?.let {
        Text("selected image $it")
    }
}

@Composable
fun OutputDirectorySelection(uiState: MainWindowState, viewModel: MainViewModel) {
    Row {
        Button(onClick = {
            viewModel.updateOutputDirectory(chooseDirectory())
        }) {
            Text("Select Output Directory")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(modifier = Modifier.padding(15.dp), text = uiState.outputDirectory?: "not selected")
    }
}

@Composable
fun ConvertToPDF(uiState: MainWindowState, viewModel: MainViewModel) {
    val imagePath = uiState.imagePath
    val outputDirPath = uiState.outputDirectory
    val targetFormat = uiState.targetFormat
    Button(onClick = {
        if (imagePath != null && outputDirPath != null) {
            val outputDir = File(outputDirPath)
            val sourceFile = File(uiState.imagePath)
            val success: Boolean = ImageConverterUseCase().execute(sourceFile,outputDir,targetFormat)
            viewModel.updateStatusMessage(if (success) "PDF created in: ${outputDir.absolutePath}" else "Failed to create PDF")
        } else {
            viewModel.updateStatusMessage("Please select an image and output directory!")
        }
    }) {
        Text("Convert to PDF")
    }
}

@Composable
fun OpenOutputFolder(outputDirectoryPath: String) {
    Button(
        modifier = Modifier.padding(top = 8.dp),
        onClick = { Desktop.getDesktop().open(File(outputDirectoryPath)) },
    ){
        Text("Open output folder")
    }
}

@Composable
fun ChooseTargetFormat(viewModel: MainViewModel) {
    var expanded by remember { mutableStateOf(false) }
    Box{
        Button(onClick = {expanded = true}){
            Text("Select target format", style = MaterialTheme.typography.h6)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }

        ) {
            ImageFormat.entries.forEach { format ->
                DropdownMenuItem(
                    onClick = {
                        viewModel.updateTargetFormat(format)
                        expanded = false
                    }
                ) {
                    Text(format.name)
                }
            }
        }
    }
}

fun chooseDirectory(): String?{
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    val chooser = JFileChooser().apply {
        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
    }

    return if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        chooser.selectedFile.absolutePath
    } else {
        null
    }
}


@Composable
fun DragDropTarget(onDragExit: () -> Unit,onDragEnter: () -> Unit, onFileDrop: (File) -> Unit) {
    val dropTarget = remember {
        object : DropTarget() {
            override fun dragEnter(dtde: DropTargetDragEvent?) {
                onDragEnter()
            }

            override fun dragExit(dte: DropTargetEvent?) {
                onDragExit()
            }

            override fun drop(evt: DropTargetDropEvent) {
                evt.acceptDrop(DnDConstants.ACTION_COPY)
                val files = evt.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
                files.firstOrNull()?.let {
                    onFileDrop(it)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        val window = java.awt.Frame.getFrames().firstOrNull()
        window?.dropTarget = dropTarget // Attach to window

        onDispose {
            window?.dropTarget = null
        }
    }
}
