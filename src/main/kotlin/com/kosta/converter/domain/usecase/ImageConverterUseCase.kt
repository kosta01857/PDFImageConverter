package com.kosta.converter.domain.usecase

import com.kosta.converter.domain.converter.ImageConverterSelector
import com.kosta.converter.core.enums.ImageFormat
import java.io.File

class ImageConverterUseCase {
    fun execute(inputFile: File, outputDir: File, format: ImageFormat): Boolean {
        return try{
            ImageConverterSelector.getConverter(format).convert(inputFile, outputDir)
        }
        catch (e: Exception){
            false
        }
    }
}