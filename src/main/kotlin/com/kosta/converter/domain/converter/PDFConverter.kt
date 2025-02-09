package com.kosta.converter.domain.converter

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import java.io.File
import javax.imageio.ImageIO

class PDFConverter: ImageConverter {
    override fun convert(inputFile: File, outputDir: File): Boolean{
        try {
            val outputFile = File(outputDir,inputFile.nameWithoutExtension + ".pdf")

            val document = PDDocument()
            val image = ImageIO.read(inputFile)
            val page = PDPage()
            document.addPage(page)

            val imageXObject = LosslessFactory.createFromImage(document, image)
            val contentStream = PDPageContentStream(document, page)
            contentStream.drawImage(imageXObject, 50f, 100f, image.width.toFloat() / 2, image.height.toFloat() / 2)
            contentStream.close()

            document.save(outputFile)
            document.close()
            return true
        }
        catch (e: Exception){
            e.printStackTrace()
            return false
        }
    }
}