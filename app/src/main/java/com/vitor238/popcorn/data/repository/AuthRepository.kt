package com.vitor238.popcorn.data.repository

import android.app.Application
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.vitor238.popcorn.R
import com.vitor238.popcorn.data.model.User
import com.vitor238.popcorn.ui.login.LoginActivity

class AuthRepository(private val application: Application) {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val userMutableLiveData: MutableLiveData<User> = MutableLiveData()
    val firebaseUserMutableLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    val loggedOutMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

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
                        updateUserInfo(task)
                    } else {
                        val defaultMessage = application.applicationContext
                            .getString(R.string.failed_to_register)

                        errorMessage.value = task.exception?.message
                            ?: defaultMessage

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
                        updateUserInfo(task)
                    } else {
                        val defaultMessage = application.applicationContext
                            .getString(R.string.failed_to_login)

                        errorMessage.value = task.exception?.message
                            ?: defaultMessage
                        Log.e(TAG, task.exception?.message ?: defaultMessage)
                    }
                })
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(ContextCompat.getMainExecutor(application.applicationContext)) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    updateUserInfo(task)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(LoginActivity.TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun updateUserInfo(task: Task<AuthResult?>) {
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
    }

    fun logout() {
        firebaseAuth.signOut()
        loggedOutMutableLiveData.postValue(true)
    }

    companion object {
        private val TAG = AuthRepository::class.simpleName
    }

}