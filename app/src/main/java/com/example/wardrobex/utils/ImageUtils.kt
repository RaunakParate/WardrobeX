package com.example.wardrobex.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

object ImageUtils {

    private const val MAX_DIMENSION = 1024  // px — keeps storage lean

    /**
     * Copies a URI (from gallery/camera) into app's internal storage.
     * Returns the absolute file path, or null on failure.
     */
    fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            val resized = resizeBitmap(bitmap)
            val fileName = "clothing_${UUID.randomUUID()}.jpg"
            val file = File(context.filesDir, fileName)

            FileOutputStream(file).use { out ->
                resized.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }

            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /** Scales down a bitmap to [MAX_DIMENSION] on its longest side, preserving aspect ratio. */
    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        if (w <= MAX_DIMENSION && h <= MAX_DIMENSION) return bitmap

        val scale = MAX_DIMENSION.toFloat() / maxOf(w, h)
        return Bitmap.createScaledBitmap(
            bitmap,
            (w * scale).toInt(),
            (h * scale).toInt(),
            true
        )
    }

    /** Deletes a clothing image from internal storage. */
    fun deleteImage(path: String) {
        val file = File(path)
        if (file.exists()) file.delete()
    }

    /** Extracts the dominant (average) color of a Bitmap as a HEX string. */
    fun getDominantColorHex(bitmap: Bitmap): String {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
        val color = scaledBitmap.getPixel(0, 0)
        scaledBitmap.recycle()
        return String.format("#%06X", (0xFFFFFF and color))
    }
}
