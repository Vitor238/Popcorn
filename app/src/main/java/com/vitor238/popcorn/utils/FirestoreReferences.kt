package com.vitor238.popcorn.utils

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreReferences {

    private val rootRef = FirebaseFirestore.getInstance()

    val usersRef: CollectionReference
        get() = rootRef.collection("users")
}