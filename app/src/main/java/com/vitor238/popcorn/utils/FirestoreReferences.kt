package com.vitor238.popcorn.utils

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirestoreReferences {

    fun getCurrentUserRef(currentUserId: String): DocumentReference {
        return Firebase.firestore.collection("users")
            .document(currentUserId)
    }

    fun getFavoritesRef(currentUserId: String): CollectionReference {
        return getCurrentUserRef(currentUserId).collection("favorites")
    }
}