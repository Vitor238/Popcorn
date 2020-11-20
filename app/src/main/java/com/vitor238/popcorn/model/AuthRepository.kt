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
    val userMutableLiveData: MutableLiveData<User> = MutableLiveData()
    val firebaseUserMutableLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    val loggedOutMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        if (firebaseAuth.currentUser != null) {
            firebaseUserMutableLiveData.postValue(firebaseAuth.currentUser)
            loggedOutMutableLiveData.postValue(false)
        }
    }

    fun register(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                ContextCompat.getMainExecutor(application.applicationContext),
                { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {

                        val newUser = task.result?.additionalUserInfo?.isNewUser
                        val currentUser = firebaseAuth.currentUser

                        val photoUrl = if (currentUser?.photoUrl != null) {
                            currentUser.photoUrl.toString()
                        } else {
                            null
                        }

                        val user = User(
                            id = currentUser?.uid,
                            name = currentUser?.displayName,
                            email = currentUser?.email,
                            photoUrl = photoUrl,
                            isNew = newUser
                        )

                        userMutableLiveData.postValue(user)


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

                        val currentUser = firebaseAuth.currentUser
                        val photoUrl = if (currentUser?.photoUrl != null) {
                            currentUser.photoUrl.toString()
                        } else {
                            null
                        }

                        val user = User(
                            id = currentUser?.uid,
                            name = currentUser?.displayName,
                            email = currentUser?.email,
                            photoUrl = photoUrl,
                            isNew = false
                        )

                        userMutableLiveData.postValue(user)

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

                        val newUser = task.result?.additionalUserInfo?.isNewUser
                        val currentUser = firebaseAuth.currentUser
                        val photoUrl = if (currentUser?.photoUrl != null) {
                            currentUser.photoUrl.toString()
                        } else {
                            null
                        }

                        val user = User(
                            id = currentUser?.uid,
                            name = currentUser?.displayName,
                            email = currentUser?.email,
                            photoUrl = photoUrl,
                            isNew = newUser
                        )

                        userMutableLiveData.postValue(user)

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