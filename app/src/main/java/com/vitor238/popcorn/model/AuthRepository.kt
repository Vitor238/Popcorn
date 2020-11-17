package com.vitor238.popcorn.model

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.vitor238.popcorn.R

class AuthRepository(private val application: Application) {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val userMutableLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    val loggedOutMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        if (firebaseAuth.currentUser != null) {
            userMutableLiveData.postValue(firebaseAuth.currentUser)
            loggedOutMutableLiveData.postValue(false)
        }
    }

    fun register(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                ContextCompat.getMainExecutor(application.applicationContext),
                { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        userMutableLiveData.postValue(firebaseAuth.currentUser)
                    } else {
                        val defaultMessage = application.applicationContext
                            .getString(R.string.failed_to_register)

                        Toast.makeText(
                            application.applicationContext, task.exception?.message
                                ?: defaultMessage, Toast.LENGTH_SHORT
                        ).show()

                        Log.e(TAG, task.exception?.message ?: defaultMessage)
                    }
                })
    }

    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                ContextCompat.getMainExecutor(application.applicationContext),
                { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        userMutableLiveData.postValue(firebaseAuth.currentUser)
                    } else {
                        val defaultMessage = application.applicationContext
                            .getString(R.string.failed_to_login)

                        Toast.makeText(
                            application.applicationContext, task.exception?.message
                                ?: defaultMessage, Toast.LENGTH_SHORT
                        ).show()
                        Log.e(TAG, task.exception?.message ?: defaultMessage)
                    }
                })
    }

    fun signInWithGoogle(googleAuthCredential: AuthCredential) {
        firebaseAuth.signInWithCredential(googleAuthCredential)
            .addOnCompleteListener(
                ContextCompat.getMainExecutor(application.applicationContext),
                { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        userMutableLiveData.postValue(firebaseAuth.currentUser)
                    } else {
                        val defaultMessage = application.applicationContext
                            .getString(R.string.failed_to_register)

                        Toast.makeText(
                            application.applicationContext, task.exception?.message
                                ?: defaultMessage, Toast.LENGTH_SHORT
                        ).show()

                        Log.e(TAG, task.exception?.message ?: defaultMessage)
                    }
                }
            )
    }

    fun logout() {
        firebaseAuth.signOut()
        loggedOutMutableLiveData.postValue(true)
    }

    companion object {
        private val TAG = AuthRepository::class.simpleName
    }

}