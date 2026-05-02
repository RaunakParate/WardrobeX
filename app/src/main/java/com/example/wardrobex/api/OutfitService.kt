package com.example.wardrobex.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object OutfitService {

    private const val API_KEY = "YOUR_API_KEY"
    private const val ENDPOINT = "https://api.openai.com/v1/chat/completions"

    suspend fun suggestOutfit(wardrobeItems: List<String>, occasion: String): String? = withContext(Dispatchers.IO) {
        try {
            val url = URL(ENDPOINT)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "Bearer $API_KEY")
            conn.doOutput = true

            val itemsString = wardrobeItems.joinToString(", ")
            val prompt = "You are a fashion stylist. I have the following clothing items in my wardrobe: $itemsString. Suggest a stylish, short outfit for a $occasion. Keep it under 2 sentences."

            val jsonBody = JSONObject().apply {
                put("model", "gpt-4o-mini")
                put("messages", JSONArray().put(
                    JSONObject().apply {
                        put("role", "user")
                        put("content", prompt)
                    }
                ))
                put("temperature", 0.7)
            }

            OutputStreamWriter(conn.outputStream).use { it.write(jsonBody.toString()) }

            if (conn.responseCode == 200) {
                val response = conn.inputStream.bufferedReader().readText()
                val jsonResponse = JSONObject(response)
                return@withContext jsonResponse
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }
}
