package com.vitor238.popcorn.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreReferences {

    private val rootRef = FirebaseFirestore.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

    val usersRef: DocumentReference
        get() = rootRef.collection("users")
            .document(currentUserUid!!)
    val favoritesRef: CollectionReference
        get() = usersRef.collection("favorites")
}