package com.vitor238.popcorn.utils

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreReferences {

    private val rootRef = FirebaseFirestore.getInstance()

    fun getCurrentUserRef(currentUserId: String): DocumentReference {
        return rootRef.collection("users")
            .document(currentUserId)
    }

    fun getFavoritesRef(currentUserId: String): CollectionReference {
        return getCurrentUserRef(currentUserId).collection("favorites")
    }
}