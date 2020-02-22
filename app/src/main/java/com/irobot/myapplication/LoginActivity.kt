package com.irobot.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.irobot.myapplication.databinding.ActivityLoginBinding
import com.irobot.myapplication.ui.auth.LoginFragment
import com.irobot.myapplication.ui.auth.SignInDialogFragment

class LoginActivity : AppCompatActivity() {
    private var binding: ActivityLoginBinding? = null
    private val isLogin = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        val loginFragment = LoginFragment()
        val signInDialogFragment = SignInDialogFragment()
        supportFragmentManager.beginTransaction().replace(R.id.login_fragment, loginFragment)
            .replace(R.id.sign_up_fragment, signInDialogFragment)
            .commit()
//         binding.loginFragment.rotat

    }

    companion object {
        private const val TAG = "MainActivity"
    }
}