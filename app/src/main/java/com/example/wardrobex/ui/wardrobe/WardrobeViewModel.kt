package com.example.wardrobex.ui.wardrobe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wardrobex.model.Clothing
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class WardrobeViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val wardrobeCollection = db.collection("wardrobe")

    private val _allClothing = MutableLiveData<List<Clothing>>()
    val allClothing: LiveData<List<Clothing>> = _allClothing

    init {
        fetchClothing()
    }

    private fun fetchClothing() {
        wardrobeCollection
            .orderBy("dateAdded", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("WardrobeViewModel", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val clothingList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Clothing::class.java)
                    }
                    _allClothing.value = clothingList
                }
            }
    }

    fun delete(clothing: Clothing) {
        if (clothing.id.isNotEmpty()) {
            wardrobeCollection.document(clothing.id)
                .delete()
                .addOnSuccessListener { Log.d("WardrobeViewModel", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w("WardrobeViewModel", "Error deleting document", e) }
        }
    }

    private val _outfitSuggestion = MutableLiveData<String?>()
    val outfitSuggestion: LiveData<String?> = _outfitSuggestion

    private val _isSuggesting = MutableLiveData<Boolean>()
    val isSuggesting: LiveData<Boolean> = _isSuggesting

    fun suggestOutfit(occasion: String) {
        val clothes = allClothing.value ?: emptyList()
        if (clothes.isEmpty()) {
            _outfitSuggestion.postValue("Your wardrobe is empty! Add some clothes first.")
            return
        }

        _isSuggesting.value = true
        val wardrobeList = clothes.map { "${it.category} (${it.color})" }

        androidx.lifecycle.viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val suggestion = com.example.wardrobex.api.OutfitService.suggestOutfit(wardrobeList, occasion)
            _isSuggesting.postValue(false)
            _outfitSuggestion.postValue(suggestion ?: "Sorry, I couldn't generate a suggestion right now.")
        }
    }
    
    fun clearSuggestion() {
        _outfitSuggestion.value = null
    }
}
