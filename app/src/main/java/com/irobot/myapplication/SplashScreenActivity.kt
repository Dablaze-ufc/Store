package com.irobot.myapplication

import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
            icon.clearAnimation()
            val transAnim = TranslateAnimation (0F, 0F, 0F, (getDisplayHeight()/2).toFloat())
            transAnim.apply {
                startOffset = 500
                duration = 3000
                fillAfter = true
                interpolator = BounceInterpolator()
                transAnim.setAnimationListener(object :
                    Animation.AnimationListener
                {
                    override fun onAnimationRepeat(animation: Animation?) {
                        TODO("Not yet implemented")
                    }

                    override fun onAnimationEnd(animation: Animation?) {
//                       icon.clearAnimation()
                        val left = icon.left
                        val top = icon.top
                        val right = icon.right
                        val bottom = icon.bottom
                        icon.layout(left,top,right,bottom)
                    }

                    override fun onAnimationStart(animation: Animation?) {
                        Log.i("TAG", "Starting button dropdown animation")
                    }

                })
                icon.startAnimation(transAnim)
            }
    }
    private fun getDisplayHeight(): Int{
        return this.resources.displayMetrics.heightPixels
    }
}

