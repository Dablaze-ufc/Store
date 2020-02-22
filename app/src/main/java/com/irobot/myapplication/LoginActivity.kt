package com.irobot.myapplication

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.irobot.myapplication.databinding.ActivityLoginBinding
import com.irobot.myapplication.ui.auth.LoginFragment
import com.irobot.myapplication.ui.auth.SignInDialogFragment
import com.irobot.myapplication.utils.LoginButton
import com.irobot.myapplication.utils.OnButtonSwitchedListener


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val isLogin = true
    private var listner: OnButtonSwitchedListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        val loginFragment = LoginFragment()
        val signInDialogFragment = SignInDialogFragment()
        supportFragmentManager.beginTransaction().replace(R.id.login_fragment, loginFragment)
            .replace(R.id.sign_up_fragment, signInDialogFragment)
            .commit()
        binding.loginFragment.rotation = -90f
        binding.button.setOnSignUpListener(signInDialogFragment)
        binding.button.setOnLoginListener(loginFragment)
        binding.button.setOnButtonSwitched(object : OnButtonSwitchedListener {
            override fun onButtonSwitched(isLogin: Boolean) {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        if (isLogin) R.color.
                    )
                )
            }

        }
        )



    }

    fun setOnButtonSwitchedlistner(listener: OnButtonSwitchedListener) {
        this.listner = listener
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}