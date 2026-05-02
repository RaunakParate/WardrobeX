package com.example.wardrobex.model

import com.google.firebase.firestore.DocumentId

/**
 * Represents a single clothing item stored in the app via Firestore.
 * [aiTags] is reserved for future AI-generated labels (stored as JSON string).
 */
data class Clothing(
    @DocumentId val id: String = "",
    val imageUrl: String = "",                          // Remote URL from Firebase Storage
    val category: String = "",                          // "Top", "Bottom", "Shoes", "Accessory", "Other"
    val color: String = "",                             // e.g. "Red", "Blue" — user-tagged or AI-derived
    val dateAdded: Long = System.currentTimeMillis(),  // Epoch ms
    val doodleUrl: String = "",
    val aiTags: String? = null                         // Reserved: future AI labels as JSON
)
