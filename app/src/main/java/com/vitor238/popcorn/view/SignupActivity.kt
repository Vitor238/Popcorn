package com.vitor238.popcorn.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.vitor238.popcorn.R
import com.vitor238.popcorn.model.User
import com.vitor238.popcorn.utils.toast
import com.vitor238.popcorn.viewmodel.LoginRegisterViewModel
import com.vitor238.popcorn.viewmodel.LoginViewModelFactory
import com.vitor238.popcorn.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.toolbar


class SignupActivity : BaseActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loginRegisterViewModel: LoginRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setupToolbar(toolbar = toolbar as Toolbar, showBackButton = true)

        initGoogleSignInClient()

        val loginViewModelFactory = LoginViewModelFactory(application)

        loginRegisterViewModel = ViewModelProvider(this, loginViewModelFactory)
            .get(LoginRegisterViewModel::class.java)

        loginRegisterViewModel.userMutableLiveData.observe(this) {
            if (it != null) {
                saveUserOnFirestore(it)
            }
        }

        button_sign_up.setOnClickListener {
            val email = edit_text_email.text.toString()
            val password = edit_text_password.text.toString()

            if (email.isNotBlank()) {
                if (password.isNotBlank()) {
                    loginRegisterViewModel.register(email, password)
                } else {
                    toast(getString(R.string.type_your_password))
                }
            } else {
                toast(getString(R.string.type_your_email))
            }
        }

        button_sign_in_with_google.setOnClickListener {
            signIn()
        }
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val googleSignInAccount = task.getResult(
                    ApiException::class.java
                )
                googleSignInAccount?.let { getGoogleAuthCredential(it) }
            } catch (e: ApiException) {
                toast(e.message ?: getString(R.string.failed_to_register))
                Log.e(TAG, e.message ?: getString(R.string.failed_to_register))
            }
        }
    }

    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        loginRegisterViewModel.signInWithGoogle(googleAuthCredential)
    }

    private fun saveUserOnFirestore(user: User) {
        val viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.saveUserOnFirestore(user)
        viewModel.firestoreUserCreatedLiveData.observe(this) { userCreated ->
            if (userCreated) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                toast(R.string.failed_to_register)
            }
        }
    }

    companion object {
        val TAG = SignupActivity::class.simpleName
        private const val RC_SIGN_IN = 1
    }
}