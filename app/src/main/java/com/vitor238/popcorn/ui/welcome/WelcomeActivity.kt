package com.vitor238.popcorn.ui.welcome

import android.content.Intent
import android.os.Bundle
import com.vitor238.popcorn.R
import com.vitor238.popcorn.ui.base.BaseActivity
import com.vitor238.popcorn.ui.home.MainActivity
import com.vitor238.popcorn.ui.login.LoginActivity
import com.vitor238.popcorn.ui.signup.SignupActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        button_sign_up.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
        button_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        text_not_now.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}