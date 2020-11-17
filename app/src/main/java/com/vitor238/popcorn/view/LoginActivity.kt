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
import com.vitor238.popcorn.utils.toast
import com.vitor238.popcorn.viewmodel.LoginRegisterViewModel
import com.vitor238.popcorn.viewmodel.LoginViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.toolbar

class LoginActivity : BaseActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loginViewModel: LoginRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupToolbar(toolbar = toolbar as Toolbar, showBackButton = true)

        initGoogleSignInClient()

        val loginViewModelFactory = LoginViewModelFactory(application)
        loginViewModel = ViewModelProvider(this, loginViewModelFactory)
            .get(LoginRegisterViewModel::class.java)
        loginViewModel.userMutableLiveData.observe(this) {
            if (it != null) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Log.i(TAG, "firebase user is null")
            }
        }

        button_login.setOnClickListener {
            val email = edit_text_email.text.toString()
            val password = edit_text_password.text.toString()
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
                Log.e(SignupActivity.TAG, e.message ?: getString(R.string.failed_to_register))
            }
        }
    }

    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        loginViewModel.signInWithGoogle(googleAuthCredential)
    }

    companion object {
        val TAG = LoginActivity::class.simpleName
        private const val RC_SIGN_IN = 1
    }
}