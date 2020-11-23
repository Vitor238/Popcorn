package com.vitor238.popcorn.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vitor238.popcorn.model.ProfileRepository
import com.vitor238.popcorn.model.User
import java.io.InputStream

class ProfileViewModel : ViewModel(), LifecycleObserver {
    private val profileRepository = ProfileRepository()
    val firestoreUserCreatedLiveData: MutableLiveData<Boolean>
    val firestoreUserLiveData: MutableLiveData<User?>
    val newPhotoUrlMutableLiveData: MutableLiveData<String?>

    fun saveUserOnFirestore(user: User) {
        profileRepository.saveUserInFirestore(user)
    }

    fun getFirestoreUser() {
        profileRepository.getCurrentFirestoreUser()
    }

    fun updateFirestoreName(newName: String) {
        profileRepository.updateFirestoreName(newName)
    }

    fun updateAuthName(newName: String) {
        profileRepository.updateAuthName(newName)
    }

    fun uploadNewProfilePicture(inputStream: InputStream) {
        profileRepository.uploadProfilePicture(inputStream)
    }

    fun updateAuthPhoto(newPhoto: String) {
        profileRepository.updateAuthPhoto(newPhoto)
    }

    fun updateFirestorePhoto(newPhoto: String) {
        profileRepository.updateFirestorePhoto(newPhoto)
    }

    init {
        firestoreUserCreatedLiveData = profileRepository.firestoreUserCreatedLiveData
        firestoreUserLiveData = profileRepository.firestoreUserMutableLiveData
        newPhotoUrlMutableLiveData = profileRepository.newPhotoUrlMutableLiveData
    }
}