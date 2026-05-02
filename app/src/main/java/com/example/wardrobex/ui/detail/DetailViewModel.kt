package com.example.wardrobex.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.wardrobex.model.Clothing
import com.google.firebase.firestore.FirebaseFirestore

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    val clothing = MutableLiveData<Clothing?>()
    val deleted = MutableLiveData<Boolean>()
    private val db = FirebaseFirestore.getInstance()

    fun loadClothing(id: String) {
        db.collection("wardrobe").document(id).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    clothing.postValue(document.toObject(Clothing::class.java))
                } else {
                    clothing.postValue(null)
                }
            }
            .addOnFailureListener {
                clothing.postValue(null)
            }
    }

    fun deleteClothing() {
        val item = clothing.value ?: return
        db.collection("wardrobe").document(item.id).delete()
            .addOnSuccessListener {
                deleted.postValue(true)
            }
    }

    fun generateDoodle() {
        val item = clothing.value ?: return
        if (item.imageUrl.isEmpty()) return

        androidx.lifecycle.viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val doodleUrl = com.example.wardrobex.api.DoodleService.transformToDoodle(item.imageUrl)
            if (doodleUrl != null) {
                // Update Firestore
                db.collection("wardrobe").document(item.id)
                    .update("doodleUrl", doodleUrl)
                    .addOnSuccessListener {
                        // Refresh the local item to trigger UI update
                        val updatedItem = item.copy(doodleUrl = doodleUrl)
                        clothing.postValue(updatedItem)
                    }
            }
        }
    }
}
