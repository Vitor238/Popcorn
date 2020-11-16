package com.vitor238.popcorn.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.vitor238.popcorn.viewmodel.LoginViewModelFactory
import com.vitor238.popcorn.R
import com.vitor238.popcorn.viewmodel.LoginRegisterViewModel
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.edit_text_email
import kotlinx.android.synthetic.main.activity_signup.edit_text_password
import kotlinx.android.synthetic.main.activity_signup.toolbar
import com.vitor238.popcorn.utils.toast

class SignupActivity : BaseActivity() {

    private lateinit var loginViewModel: LoginRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setupToolbar(toolbar = toolbar as Toolbar,
            showBackButton = true)

        val loginViewModelFactory = LoginViewModelFactory(application)
        val loginViewModel: LoginRegisterViewModel = ViewModelProvider(this,loginViewModelFactory)
            .get(LoginRegisterViewModel::class.java)
        loginViewModel.userMutableLiveData.observe(this){
            if(it != null){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }else{
                Log.i(TAG, "firebase user is null")
            }
        }

        button_sign_up.setOnClickListener {
            val email = edit_text_email.text.toString()
            val password = edit_text_password.text.toString()

            if (email.isNotBlank()){
                if(password.isNotBlank()){
                    loginViewModel.register(email,password)
                }else{
                    toast(getString(R.string.type_your_password))
                }
            }else{
                toast(getString(R.string.type_your_email))
            }
        }
    }

    companion object{
        val TAG = SignupActivity::class.simpleName
    }
}