package com.vitor238.popcorn.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.vitor238.popcorn.utils.FirestoreRefs

class ProfileRepository {

    val firestoreUserCreatedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val firestoreUserMutableLiveData: MutableLiveData<User?> = MutableLiveData()

    fun getCurrentFirestoreUser() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val currentUserRef = FirestoreRefs.usersRef.document(currentUser.uid)
            currentUserRef.get().addOnCompleteListener { task ->
                val user: User? = task.result?.toObject(User::class.java)
                firestoreUserMutableLiveData.value = user
            }
        } else {
            firestoreUserMutableLiveData.value = null
        }
    }

    fun saveUserInFirestore(user: User) {
        val newUserRef = FirestoreRefs.usersRef.document(user.id!!)
        newUserRef.set(user).addOnSuccessListener {
            firestoreUserCreatedLiveData.value = true
        }.addOnFailureListener { e ->
            firestoreUserCreatedLiveData.value = false
            Log.w(TAG, "Error adding document", e)
        }
    }

    companion object {
        private val TAG = ProfileRepository::class.simpleName
    }
}