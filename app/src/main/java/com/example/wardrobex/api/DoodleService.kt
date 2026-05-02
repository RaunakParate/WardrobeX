package com.example.wardrobex.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

object DoodleService {

    // Final Prompt: Optimized and Reusable for consistent doodles
    private const val DOODLE_PROMPT = "flat 2D vector doodle illustration of this clothing item, minimalist, thick black outlines, solid pastel colors, white background, clean, sticker style, no shading"

    /**
     * Converts a clothing image to a doodle illustration using Stability AI,
     * uploads the result to Firebase Storage, and returns the URL.
     *
     * @param imageUrl Original clothing image URL
     * @return URL of the generated doodle, or null if failed
     */
    suspend fun transformToDoodle(imageUrl: String): String? = withContext(Dispatchers.IO) {
        try {
            // 1. Download image and convert to Base64
            val inputStream = URL(imageUrl).openStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val baos = ByteArrayOutputStream()
            // Resize if needed, but SD expects specific dimensions. 
            // For simplicity, compressing to PNG.
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val base64Image = Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)

            // 2. Call Stability AI Image-to-Image API
            val url = URL("https://api.stability.ai/v1/generation/stable-diffusion-xl-1024-v1-0/image-to-image")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")
            // REPLACE "YOUR_API_KEY" with a real Stability AI key
            conn.setRequestProperty("Authorization", "Bearer YOUR_API_KEY")
            conn.doOutput = true

            // Build JSON Body
            val jsonBody = JSONObject().apply {
                put("init_image", base64Image)
                put("init_image_mode", "IMAGE_STRENGTH")
                put("image_strength", 0.35) // 0.0 to 1.0; 0.35 gives strong transformation while keeping shape
                put("text_prompts", JSONArray().put(JSONObject().put("text", DOODLE_PROMPT)))
            }

            OutputStreamWriter(conn.outputStream).use { it.write(jsonBody.toString()) }

            // Check response
            if (conn.responseCode == 200) {
                val response = conn.inputStream.bufferedReader().readText()
                val resultJson = JSONObject(response)
                val artifacts = resultJson.getJSONArray("artifacts")
                if (artifacts.length() > 0) {
                    val outputBase64 = artifacts.getJSONObject(0).getString("base64")
                    
                    // 3. Decode and Upload to Firebase Storage
                    val outputBytes = Base64.decode(outputBase64, Base64.DEFAULT)
                    val storageRef = FirebaseStorage.getInstance().reference.child("doodles/${UUID.randomUUID()}.png")
                    
                    Tasks.await(storageRef.putBytes(outputBytes))
                    val downloadUri = Tasks.await(storageRef.downloadUrl)
                    
                    return@withContext downloadUri.toString()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }
}
