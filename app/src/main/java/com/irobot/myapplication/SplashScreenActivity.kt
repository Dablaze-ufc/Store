package com.irobot.myapplication

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
            if (savedInstanceState == null){
                val viewTreeObserver:ViewTreeObserver = splash.viewTreeObserver
                if (viewTreeObserver.isAlive){
                    viewTreeObserver.addOnGlobalLayoutListener(object :
                        ViewTreeObserver.OnGlobalLayoutListener{
                        override fun onGlobalLayout() {
                            animationsSlash()
                            icon.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            }

                    })
                }
            }


    }

    private fun animationsSlash(){
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
                    val cx = icon.measuredWidth/2
                    val cy = icon.measuredHeight / 2

                    val finalRadius = Math.max(icon.width, icon.height)/2
                    val anim = ViewAnimationUtils.createCircularReveal(icon,cx,cy,
                    0F,
                        finalRadius.toFloat()
                    )

                    anim.addListener(object: AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            icon.visibility = INVISIBLE
                        }
                    })

                    anim.start()
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

