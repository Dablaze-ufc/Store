package com.irobot.myapplication

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    private val beerDepth: Long = 10000

    private val pourBeerTaskNew = object : CountDownTimer(beerDepth, 10) {
        override fun onFinish() {

        }

        override fun onTick(millisUntilFinished: Long) {
            val progress = beerDepth - millisUntilFinished
            val percentage = (progress.toDouble() / beerDepth * 100).toInt()
            when (percentage) {
                in 0..90 -> beerProgressView.beerProgress = percentage
                else -> onFinish()
            }
        }
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
            pourBeerTaskNew.start()
    }
}

