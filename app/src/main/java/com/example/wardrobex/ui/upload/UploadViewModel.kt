package com.example.wardrobex.ui.upload

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.wardrobex.model.Clothing
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class UploadViewModel(application: Application) : AndroidViewModel(application) {


    /** URI selected from gallery or captured by camera — drives image preview */
    val selectedImageUri = MutableLiveData<Uri?>()

    /** Emits true when save completes successfully */
    val saveSuccess = MutableLiveData<Boolean>()

    /** Emits an error message if save fails */
    val errorMessage = MutableLiveData<String?>()

    /** Tracks uploading state to show progress in UI */
    val isUploading = MutableLiveData<Boolean>(false)

    fun onImageSelected(uri: Uri) {
        selectedImageUri.value = uri
    }

    fun saveClothing(category: String, color: String) {
        val uri = selectedImageUri.value
        if (uri == null) {
            errorMessage.postValue("Please select an image first.")
            return
        }
        
        isUploading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            var finalColor = color.trim()
            if (finalColor.isEmpty()) {
                try {
                    val inputStream = getApplication<Application>().contentResolver.openInputStream(uri)
                    val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    if (bitmap != null) {
                        finalColor = com.example.wardrobex.utils.ImageUtils.getDominantColorHex(bitmap)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val imageId = UUID.randomUUID().toString()
            val storageRef = FirebaseStorage.getInstance().reference.child("images/$imageId.jpg")

            storageRef.putFile(uri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        saveMetadataToFirestore(imageId, downloadUrl.toString(), category, finalColor)
                    }
                }
                .addOnFailureListener { e ->
                    isUploading.postValue(false)
                    errorMessage.postValue("Upload failed: ${e.message}")
                }
        }
    }

    private fun saveMetadataToFirestore(id: String, imageUrl: String, category: String, color: String) {
        val clothingItem = hashMapOf(
            "id" to id,
            "imageUrl" to imageUrl,
            "category" to category,
            "color" to color
        )

        FirebaseFirestore.getInstance().collection("wardrobe")
            .document(id)
            .set(clothingItem)
            .addOnSuccessListener {
                isUploading.postValue(false)
                saveSuccess.postValue(true)
            }
            .addOnFailureListener { e ->
                isUploading.postValue(false)
                errorMessage.postValue("Failed to save data: ${e.message}")
            }
    }

    fun clearSelection() {
        selectedImageUri.value = null
    }
}
