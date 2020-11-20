package com.vitor238.popcorn.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vitor238.popcorn.model.ProfileRepository
import com.vitor238.popcorn.model.User

class ProfileViewModel : ViewModel() {
    val profileRepository = ProfileRepository()
    val firestoreUserCreatedLiveData: MutableLiveData<Boolean>
    val firestoreUserLiveData: MutableLiveData<User?>


    fun saveUserOnFirestore(user: User) {
        profileRepository.saveUserInFirestore(user)
    }

    fun getFirestoreUser() {
        profileRepository.getCurrentFirestoreUser()
    }

    init {
        firestoreUserCreatedLiveData = profileRepository.firestoreUserCreatedLiveData
        firestoreUserLiveData = profileRepository.firestoreUserMutableLiveData
    }
}