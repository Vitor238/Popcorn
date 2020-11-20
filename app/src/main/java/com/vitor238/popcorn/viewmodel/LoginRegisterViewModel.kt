package com.vitor238.popcorn.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.vitor238.popcorn.model.AuthRepository
import com.vitor238.popcorn.model.User

class LoginRegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository: AuthRepository = AuthRepository(application)
    val userMutableLiveData: MutableLiveData<User>

    fun register(email: String, password: String) {
        authRepository.register(email, password)
    }

    fun login(email: String, password: String) {
        authRepository.login(email, password)
    }

    fun signInWithGoogle(googleAuthCredential: AuthCredential) {
        authRepository.signInWithGoogle(googleAuthCredential)
    }

    init {
        userMutableLiveData = authRepository.userMutableLiveData
    }
}