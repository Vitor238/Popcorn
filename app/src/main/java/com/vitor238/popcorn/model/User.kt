package com.vitor238.popcorn.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val id: String? = "",
    val name: String? = "",
    val email: String? = "",
    val photoUrl: String? = "",
    @get:Exclude
    val isNew: Boolean? = false
)