package com.vitor238.popcorn.model

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.ListenerRegistration
import com.vitor238.popcorn.utils.FirebaseStorageReferences
import com.vitor238.popcorn.utils.FirestoreReferences
import java.io.InputStream

class ProfileRepository {

    val firestoreUserCreatedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val firestoreUserMutableLiveData: MutableLiveData<User?> = MutableLiveData()
    val newPhotoUrlMutableLiveData: MutableLiveData<String?> = MutableLiveData()
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private var currentUserListener: ListenerRegistration? = null

    fun getCurrentFirestoreUser() {
        //TODO: detach
        if (currentUser != null) {
            val currentUserRef = FirestoreReferences.usersRef.document(currentUser.uid)
            currentUserListener = currentUserRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    firestoreUserMutableLiveData.value = snapshot.toObject(User::class.java)
                } else {
                    Log.d(TAG, "Data: null")
                    firestoreUserMutableLiveData.value = null
                }
            }

        } else {
            firestoreUserMutableLiveData.value = null
        }
    }

    fun saveUserInFirestore(user: User) {
        val newUserRef = FirestoreReferences.usersRef.document(user.id!!)
        newUserRef.set(user).addOnSuccessListener {
            firestoreUserCreatedLiveData.value = true
        }.addOnFailureListener { e ->
            firestoreUserCreatedLiveData.value = false
            Log.w(TAG, "Error adding document", e)
        }
    }


    fun uploadProfilePicture(inputStream: InputStream) {
        val imageRef = FirebaseStorageReferences.myProfileRef
            .child(PROFILE + IMG_EXTENSION)

        val uploadTask = imageRef.putStream(inputStream)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { photoUrl ->
                newPhotoUrlMutableLiveData.value = photoUrl.toString()
            }
        }.addOnFailureListener {
            newPhotoUrlMutableLiveData.value = null
        }
    }

    fun updateFirestorePhoto(newPhoto: String) {
        val newUserRef = FirestoreReferences.usersRef.document(currentUser?.uid!!)
        newUserRef.update("photoUrl", newPhoto).addOnSuccessListener {
            Log.i(TAG, "Photo updated")
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error adding document", e)
        }
    }

    fun updateFirestoreName(newName: String) {
        val newUserRef = FirestoreReferences.usersRef.document(currentUser?.uid!!)
        newUserRef.update("name", newName).addOnSuccessListener {
            Log.i(TAG, "Name updated")
        }.addOnFailureListener { e ->
            Log.w(TAG, "Error adding document", e)
        }
    }

    fun updateAuthName(newName: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build()

        currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                Log.i(TAG, "Auth name updated: ${task.isSuccessful}")
            }
    }

    fun updateAuthPhoto(newPhoto: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(newPhoto.toUri())
            .build()

        currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                Log.i(TAG, "Auth photo updated: ${task.isSuccessful}")
            }
    }

    companion object {
        private val TAG = ProfileRepository::class.simpleName
        private const val PROFILE = "profile"
        private const val IMG_EXTENSION = ".jpg"
    }
}