package com.irobot.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
//            val bounce = AnimationUtils.loadAnimation(this,R.anim.bounce)
            icon.clearAnimation()
//            val transAnim: TranslateAnimation (0,0,0,getDisplayHeight()/2)

    }
    fun getDisplayHeight(): Int{
        return this.resources.displayMetrics.heightPixels
    }
}

