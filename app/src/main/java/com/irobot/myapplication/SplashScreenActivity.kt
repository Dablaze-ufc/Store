package com.irobot.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.VISIBLE
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.github.ybq.android.spinkit.style.DoubleBounce
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
            if (savedInstanceState == null){
                val viewTreeObserver:ViewTreeObserver = icon.viewTreeObserver
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
        val transAnim2 = TranslateAnimation (0F, 0F, 0F, (getDisplayHeight()/2).toFloat())
        transAnim2.apply {
            startOffset = 500
            duration = 2000
            fillAfter = true
            interpolator = BounceInterpolator()
            transAnim2.setAnimationListener(object :
                Animation.AnimationListener
            {
                override fun onAnimationRepeat(animation: Animation?) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationEnd(animation: Animation?) {
//                       icon.clearAnimation()
                    val left = spin_kit.left
                    val top = spin_kit.top
                    val right = spin_kit.right
                    val bottom = spin_kit.bottom


                    spin_kit.layout(left,top,right,bottom)
//                    val sprite = DoubleBounce()
//                    spin_kit.setIndeterminateDrawable(sprite)
//                    spin_kit.visibility = VISIBLE
                }

                override fun onAnimationStart(animation: Animation?) {
                    Log.i("TAG", "Starting button dropdown animation")
                }

            })
        icon.clearAnimation()
        val transAnim = TranslateAnimation (0F, 0F, 0F, (getDisplayHeight()/2).toFloat())
        transAnim.apply {
            startOffset = 500
            duration = 2000
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

                    val sprite = DoubleBounce()
                    spin_kit.setIndeterminateDrawable(sprite)
                    spin_kit.visibility = VISIBLE
                    icon.layout(left,top,right,bottom)


                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()

                }

                override fun onAnimationStart(animation: Animation?) {
                    icon.visibility = VISIBLE
                }

            })
            icon.startAnimation(transAnim)
//            spin_kit.startAnimation(transAnim2)
//            val sprite = DoubleBounce()
//            spin_kit.setIndeterminateDrawable(sprite)
//            spin_kit.visibility = VISIBLE
        }

    }
    }
    private fun getDisplayHeight(): Int{
        return this.resources.displayMetrics.heightPixels
    }
}

