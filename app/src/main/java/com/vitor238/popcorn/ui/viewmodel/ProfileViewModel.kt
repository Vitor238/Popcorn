package com.vitor238.popcorn.ui.viewmodel

import androidx.lifecycle.*
import com.vitor238.popcorn.data.model.User
import com.vitor238.popcorn.data.repository.ProfileRepository
import java.io.InputStream

class ProfileViewModel : ViewModel(), LifecycleObserver {
    private val profileRepository = ProfileRepository()
    val firestoreUserCreatedLiveData: MutableLiveData<Boolean> = profileRepository
        .firestoreUserCreatedLiveData
    val firestoreUserLiveData: MutableLiveData<User?> = profileRepository
        .firestoreUserMutableLiveData
    val newPhotoUrlMutableLiveData: MutableLiveData<String?> = profileRepository
        .newPhotoUrlMutableLiveData

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

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun removeFirestoreUserListener() {
        profileRepository.detachCurrentUserListener()
    }

}