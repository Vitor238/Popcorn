package com.vitor238.popcorn.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.vitor238.popcorn.data.model.User
import com.vitor238.popcorn.data.repository.AuthRepository

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository: AuthRepository = AuthRepository(application)
    val userMutableLiveData: LiveData<User>
        get() = authRepository.userMutableLiveData

    val errorMessage: LiveData<String>
        get() = authRepository.errorMessage

    fun register(email: String, password: String) {
        authRepository.register(email, password)
    }

    fun login(email: String, password: String) {
        authRepository.login(email, password)
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        authRepository.firebaseAuthWithGoogle(idToken)
    }

}