package com.kosta.converter.domain.converter

import java.io.File

interface ImageConverter {
    fun convert(inputFile: File, outputDir: File): Boolean
}