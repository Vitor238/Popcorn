package com.vitor238.popcorn.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.vitor238.popcorn.R
import com.vitor238.popcorn.data.model.User
import com.vitor238.popcorn.databinding.ActivityLoginBinding
import com.vitor238.popcorn.ui.base.BaseActivity
import com.vitor238.popcorn.ui.home.MainActivity
import com.vitor238.popcorn.ui.signup.SignUpActivity
import com.vitor238.popcorn.ui.viewmodel.LoginRegisterViewModel
import com.vitor238.popcorn.ui.viewmodel.LoginViewModelFactory
import com.vitor238.popcorn.ui.viewmodel.ProfileViewModel
import com.vitor238.popcorn.utils.toast

class LoginActivity : BaseActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loginViewModel: LoginRegisterViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(toolbar = binding.toolbar.toolbarLogo, showBackButton = true)

        initGoogleSignInClient()

        val loginViewModelFactory = LoginViewModelFactory(application)
        loginViewModel = ViewModelProvider(this, loginViewModelFactory)
            .get(LoginRegisterViewModel::class.java)
        loginViewModel.userMutableLiveData.observe(this) {
            if (it != null) {
                if (it.isNew == true) {
                    saveUserOnFirestore(it)
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            } else {
                Log.i(TAG, "firebase user is null")
            }
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (email.isNotBlank()) {
                if (password.isNotBlank()) {
                    loginViewModel.login(email, password)
                } else {
                    toast(getString(R.string.type_your_password))
                }
            } else {
                toast(getString(R.string.type_your_email))
            }
        }

        binding.buttonSignInWithGoogle.setOnClickListener {
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
                Log.e(SignUpActivity.TAG, e.message ?: getString(R.string.failed_to_register))
            }
        }
    }

    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        loginViewModel.signInWithGoogle(googleAuthCredential)
    }

    private fun saveUserOnFirestore(user: User) {
        val viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.saveUserOnFirestore(user)
        viewModel.firestoreUserCreatedLiveData.observe(this) { userCreated ->
            if (userCreated) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                toast(R.string.failed_to_register)
            }

        }
    }


    companion object {
        val TAG = LoginActivity::class.simpleName
        private const val RC_SIGN_IN = 1
    }
}