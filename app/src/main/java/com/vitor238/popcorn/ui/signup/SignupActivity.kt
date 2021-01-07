package com.vitor238.popcorn.ui.signup

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
import com.vitor238.popcorn.databinding.ActivitySignupBinding
import com.vitor238.popcorn.ui.base.BaseActivity
import com.vitor238.popcorn.ui.home.MainActivity
import com.vitor238.popcorn.ui.viewmodel.LoginRegisterViewModel
import com.vitor238.popcorn.ui.viewmodel.LoginViewModelFactory
import com.vitor238.popcorn.ui.viewmodel.ProfileViewModel
import com.vitor238.popcorn.utils.toast


class SignupActivity : BaseActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loginRegisterViewModel: LoginRegisterViewModel
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar.toolbarLogo, showBackButton = true)

        initGoogleSignInClient()

        val loginViewModelFactory = LoginViewModelFactory(application)

        loginRegisterViewModel = ViewModelProvider(this, loginViewModelFactory)
            .get(LoginRegisterViewModel::class.java)

        loginRegisterViewModel.userMutableLiveData.observe(this) {
            if (it != null) {
                saveUserOnFirestore(it)
            }
        }

        binding.buttonSignUp.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

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
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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