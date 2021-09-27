package com.vitor238.popcorn.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.vitor238.popcorn.BuildConfig
import com.vitor238.popcorn.R
import com.vitor238.popcorn.databinding.ActivityLoginBinding
import com.vitor238.popcorn.ui.base.BaseActivity
import com.vitor238.popcorn.ui.home.MainActivity
import com.vitor238.popcorn.ui.viewmodel.AuthViewModel
import com.vitor238.popcorn.ui.viewmodel.AuthViewModelFactory
import com.vitor238.popcorn.ui.viewmodel.ProfileViewModel

class LoginActivity : BaseActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(toolbar = binding.toolbar.toolbarLogo, showBackButton = true)
        initGoogleSignInClient()
        setupGoogleSignInIntent()
        setupViews()

        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.firestoreUserCreatedLiveData.observe(this) { userCreated ->
            if (userCreated) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                showSnackBar(binding.root, R.string.failed_to_register)
            }
        }

        val authViewModelFactory = AuthViewModelFactory(application)
        authViewModel = ViewModelProvider(this, authViewModelFactory)
            .get(AuthViewModel::class.java)
        authViewModel.userMutableLiveData.observe(this) {
            if (it != null) {
                if (it.isNew == true) {
                    profileViewModel.saveUserOnFirestore(it)
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            } else {
                Log.i(TAG, "firebase user is null")
                showSnackBar(binding.root, R.string.failed_to_login)
            }
        }

        authViewModel.errorMessage.observe(this) {
            showSnackBar(binding.root, it)
        }
    }

    private fun setupViews() {
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            if (email.isNotBlank()) {
                if (password.isNotBlank()) {
                    authViewModel.login(email, password)
                } else {
                    showSnackBar(binding.buttonLogin, R.string.type_your_password)
                }
            } else {
                showSnackBar(binding.buttonLogin, R.string.type_your_email)
            }
        }

        binding.buttonSignInWithGoogle.setOnClickListener {
            startForResult.launch(googleSignInClient.signInIntent)
        }
    }

    private fun setupGoogleSignInIntent() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            val account = task.getResult(ApiException::class.java)!!
                            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                            authViewModel.firebaseAuthWithGoogle(account.idToken!!)
                        } catch (e: ApiException) {
                            // Google Sign In failed, update UI appropriately
                            Log.w(TAG, "Google sign in failed", e)
                            showSnackBar(binding.root, R.string.failed_to_login)
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.e(TAG, "Result Canceled ")
                        showSnackBar(binding.root, R.string.failed_to_login)
                    }
                }
            }
    }

    private fun initGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.DEFAULT_WEB_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    companion object {
        val TAG = LoginActivity::class.simpleName
    }
}