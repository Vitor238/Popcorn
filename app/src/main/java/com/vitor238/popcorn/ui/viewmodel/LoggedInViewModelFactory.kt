package com.vitor238.popcorn.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoggedInViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoggedInViewModel::class.java)) {
            return LoggedInViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
