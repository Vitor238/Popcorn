package com.vitor238.popcorn.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.vitor238.popcorn.model.AuthRepository

class LoggedInViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository: AuthRepository = AuthRepository(application)
    val firebaseUserMutableLiveData: MutableLiveData<FirebaseUser>
    val loggedOutMutableLiveData: MutableLiveData<Boolean>
    fun logOut() {
        authRepository.logout()
    }

    init {
        firebaseUserMutableLiveData = authRepository.firebaseUserMutableLiveData
        loggedOutMutableLiveData = authRepository.loggedOutMutableLiveData
    }
}