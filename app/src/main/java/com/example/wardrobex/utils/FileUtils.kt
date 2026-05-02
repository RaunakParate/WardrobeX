package com.example.wardrobex.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object FileUtils {

    /**
     * Creates an empty temp file in internal cache dir for the camera intent.
     * Returns a content URI exposed via FileProvider.
     */
    fun createImageUri(context: Context): Pair<Uri, File> {
        val imageFile = File.createTempFile(
            "temp_camera_",
            ".jpg",
            context.cacheDir
        )
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            imageFile
        )
        return Pair(uri, imageFile)
    }

    /** Returns the File object for a given absolute path. */
    fun fileFromPath(path: String): File = File(path)
}
