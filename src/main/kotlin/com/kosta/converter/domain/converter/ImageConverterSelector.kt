package com.kosta.converter.domain.converter

import com.kosta.converter.core.enums.ImageFormat

object ImageConverterSelector {
    fun getConverter(format: ImageFormat): ImageConverter {
        return when (format) {
            ImageFormat.PDF -> PDFConverter()
            else -> TODO("not yet implemented")
        }
    }
}