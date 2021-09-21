package com.vitor238.popcorn.ui.signup

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
import com.vitor238.popcorn.R
import com.vitor238.popcorn.databinding.ActivitySignUpBinding
import com.vitor238.popcorn.ui.base.BaseActivity
import com.vitor238.popcorn.ui.home.MainActivity
import com.vitor238.popcorn.ui.login.LoginActivity
import com.vitor238.popcorn.ui.viewmodel.AuthViewModel
import com.vitor238.popcorn.ui.viewmodel.AuthViewModelFactory
import com.vitor238.popcorn.ui.viewmodel.ProfileViewModel
import com.vitor238.popcorn.utils.ApiKeys


class SignUpActivity : BaseActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar.toolbarLogo, showBackButton = true)
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
                profileViewModel.saveUserOnFirestore(it)
            } else {
                Log.i(LoginActivity.TAG, "firebase user is null")
                showSnackBar(binding.root, R.string.failed_to_register)
            }
        }

        authViewModel.errorMessage.observe(this) {
            showSnackBar(binding.root, it)
        }
    }

    private fun setupViews() {
        binding.buttonSignUp.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (email.isNotBlank()) {
                if (password.isNotBlank()) {
                    authViewModel.register(email, password)
                } else {
                    showSnackBar(binding.buttonSignUp, R.string.type_your_password)
                }
            } else {
                showSnackBar(binding.buttonSignUp, R.string.type_your_email)
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
                            Log.d(LoginActivity.TAG, "firebaseAuthWithGoogle:" + account.id)
                            authViewModel.firebaseAuthWithGoogle(account.idToken!!)
                        } catch (e: ApiException) {
                            // Google Sign In failed, update UI appropriately
                            Log.w(LoginActivity.TAG, "Google sign in failed", e)
                            showSnackBar(binding.root, R.string.failed_to_register)
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        Log.e(LoginActivity.TAG, "Result Canceled ")
                        showSnackBar(binding.root, R.string.failed_to_register)
                    }
                }
            }
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ApiKeys.DEFAULT_WEB_CLIENT_ID)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    companion object {
        val TAG = SignUpActivity::class.simpleName
    }
}