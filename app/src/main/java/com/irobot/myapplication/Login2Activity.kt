package com.irobot.myapplication


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.irobot.myapplication.databinding.ActivityLogin2Binding
import com.irobot.myapplication.ui.auth.RegisterFragment
import com.irobot.myapplication.ui.auth.SignInFragment
import com.irobot.myapplication.utils.FlexibleFrameLayout.Companion.ORDER_LOGIN_STATE
import com.irobot.myapplication.utils.FlexibleFrameLayout.Companion.ORDER_SIGN_UP_STATE
import com.irobot.myapplication.utils.OnButtonSwitchedListener

class Login2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLogin2Binding
    private var isLogin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_login2)

        val registerFragment = RegisterFragment()
        val signInFragment = SignInFragment()
        supportFragmentManager.beginTransaction().replace(R.id.sign_up_fragment, registerFragment)
            .replace(R.id.login_fragment, signInFragment)
            .commit()
        binding.loginFragment.rotation = -90f
        binding.button.setOnSignUpListener(registerFragment)
        binding.button.setOnLoginListener(signInFragment)
        binding.button.setOnClickListener { view ->
            switchFragment(view)
        }
        binding.button.setOnButtonSwitched(object : OnButtonSwitchedListener {
            override fun onButtonSwitched(isLogin: Boolean) {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        this@Login2Activity,
                        if (isLogin) R.color.colorPrimary else R.color.secondPage
                    )
                )
            }

        })
        binding.loginFragment.visibility = INVISIBLE
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        binding.loginFragment.pivotX = (binding.loginFragment.width / 2).toFloat()
        binding.loginFragment.pivotY = binding.loginFragment.height.toFloat()
        binding.signUpFragment.pivotX = (binding.signUpFragment.width / 2).toFloat()
        binding.signUpFragment.pivotY = binding.signUpFragment.height.toFloat()


    }

    fun switchFragment(view: View) {
        if (isLogin) {
            binding.loginFragment.visibility = VISIBLE
            binding.loginFragment.animate().rotation(0f).setListener(object :
                AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    binding.signUpFragment.visibility = INVISIBLE
                    binding.signUpFragment.rotation = 90f
                    binding.wrapper.setDrawOrder(ORDER_LOGIN_STATE)
                }


            })
        } else {
            binding.signUpFragment.visibility = VISIBLE
            binding.signUpFragment.animate().rotation(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        binding.loginFragment.visibility = INVISIBLE
                        binding.loginFragment.rotation = -90f
                        binding.wrapper.setDrawOrder(ORDER_SIGN_UP_STATE)
                    }
                })
        }
        isLogin = !isLogin
        binding.button.startAnimation()
    }
}
