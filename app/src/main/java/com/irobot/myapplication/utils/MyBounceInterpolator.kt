package com.irobot.myapplication.utils

import android.view.animation.Interpolator

internal class MyBounceInterpolator(amp: Double, freq: Double) :
    Interpolator {
    private var amplitude = 1.0
    private var frequency = 10.0
    override fun getInterpolation(time: Float): Float {
        return (-1 * Math.pow(Math.E, -time / amplitude) * Math.cos(
            frequency * time
        ) + 1).toFloat()
    }

    init {
        amplitude = amp
        frequency = freq
    }
}