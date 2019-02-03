package com.booksb.imageprocessing

import java.awt.AlphaComposite
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO


class ImageResizer {

    companion object {
        @JvmStatic
        fun resize(inputStream: InputStream, formatName: String, width: Int, height: Int): ByteArrayOutputStream {
            val inputImage = ImageIO.read(inputStream)

            val outputImage = BufferedImage(width, height, inputImage.type)

            outputImage.createGraphics().apply {
                composite = AlphaComposite.Src
                setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
                setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
                setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                drawImage(inputImage, 0, 0, width, height, null)
                dispose()
            }

            val out = ByteArrayOutputStream()
            ImageIO.write(outputImage, formatName, out)
            return out
        }
    }
}