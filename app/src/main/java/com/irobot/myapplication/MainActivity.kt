package com.irobot.myapplication

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.irobot.myapplication.utils.CurvedBottomNavigationView
import com.sdsmdg.harjot.vectormaster.VectorMasterView
import com.sdsmdg.harjot.vectormaster.models.PathModel

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var bottomNavView: CurvedBottomNavigationView
    private lateinit var store: VectorMasterView
    private lateinit var profile: VectorMasterView
    private lateinit var cart: VectorMasterView
    private lateinit var lin_id: RelativeLayout
    private lateinit var outline: PathModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavView = findViewById(R.id.bottom_nav)
        store = findViewById(R.id.store)
        cart = findViewById(R.id.cart)
        profile = findViewById(R.id.profile)
        lin_id = findViewById(R.id.frgmentContainer)
        bottomNavView.inflateMenu(R.menu.nav_menu)
        bottomNavView.setOnNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_cart -> {
                draw(6)
                lin_id.x = bottomNavView.mFirstCurveControlPoint1.x.toFloat()
                cart.visibility = VISIBLE
                profile.visibility = GONE
                store.visibility = GONE
                drawAnimation(cart)
            }
            R.id.action_profile -> {
                draw()
                lin_id.x = bottomNavView.mFirstCurveControlPoint1.x.toFloat()
                cart.visibility = GONE
                profile.visibility = VISIBLE
                store.visibility = GONE
                drawAnimation(profile)
            }
            R.id.action_store -> {
                draw(2)
                lin_id.x = bottomNavView.mFirstCurveControlPoint1.x.toFloat()
                cart.visibility = GONE
                profile.visibility = GONE
                store.visibility = VISIBLE
                drawAnimation(store)
            }

        }
        return true
    }

    private fun draw() {
        bottomNavView.mFirstCurveStartPoint.set(
            (bottomNavView.mNavigationBarWidth * 10 / 12) - (bottomNavView.CURVE_CIRCLE_RADIUS * 2) -
                    (bottomNavView.CURVE_CIRCLE_RADIUS / 3), 0
        )

        bottomNavView.mFirstCurveEndPoint.set(
            bottomNavView.mNavigationBarWidth * 10 / 12, bottomNavView.CURVE_CIRCLE_RADIUS
                    + (bottomNavView.CURVE_CIRCLE_RADIUS / 4)
        )

        bottomNavView.mSecondCurveStartPoint = bottomNavView.mFirstCurveStartPoint
        bottomNavView.mSecondCurveEndPoint.set(
            (bottomNavView.mNavigationBarWidth * 10 / 12) + (bottomNavView.CURVE_CIRCLE_RADIUS * 2) + (bottomNavView.CURVE_CIRCLE_RADIUS / 3),
            0
        )

        bottomNavView.mFirstCurveControlPoint1.set(
            bottomNavView.mFirstCurveStartPoint.x + bottomNavView.CURVE_CIRCLE_RADIUS + (bottomNavView.CURVE_CIRCLE_RADIUS / 4),
            bottomNavView.mFirstCurveStartPoint.y
        )

        bottomNavView.mFirstCurveControlPoint2.set(
            bottomNavView.mFirstCurveEndPoint.x - (bottomNavView.CURVE_CIRCLE_RADIUS * 2) + bottomNavView.CURVE_CIRCLE_RADIUS,
            bottomNavView.mFirstCurveEndPoint.y
        )


        bottomNavView.mSecondCurveControlPoint1.set(
            bottomNavView.mSecondCurveStartPoint.x + (bottomNavView.CURVE_CIRCLE_RADIUS * 2) -
                    bottomNavView.CURVE_CIRCLE_RADIUS, bottomNavView.mSecondCurveStartPoint.y
        )

        bottomNavView.mSecondCurveControlPoint2.set(
            bottomNavView.mSecondCurveEndPoint.x - (bottomNavView.CURVE_CIRCLE_RADIUS + (bottomNavView.CURVE_CIRCLE_RADIUS / 4)),
            bottomNavView.mSecondCurveEndPoint.y
        )

    }

    private fun drawAnimation(cart: VectorMasterView) {
        outline = cart.getPathModelByName("outline")
        outline.strokeColor = Color.WHITE
        outline.trimPathEnd = 0.0f
        val valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        valueAnimator.setDuration(1000)
        valueAnimator.addUpdateListener(object : AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                outline.trimPathEnd = animation!!.animatedValue as Float
                cart.update()
            }
        })

        valueAnimator.start()

    }

    private fun draw(i: Int) {
        bottomNavView.mFirstCurveStartPoint.set(
            (bottomNavView.mNavigationBarWidth / i) -
                    (bottomNavView.CURVE_CIRCLE_RADIUS * 2) - (bottomNavView.CURVE_CIRCLE_RADIUS / 3),
            0
        )

        bottomNavView.mFirstCurveEndPoint.set(
            bottomNavView.mNavigationBarWidth / i, bottomNavView.CURVE_CIRCLE_RADIUS
                    + (bottomNavView.CURVE_CIRCLE_RADIUS / 4)
        )

        bottomNavView.mSecondCurveStartPoint = bottomNavView.mFirstCurveStartPoint
        bottomNavView.mSecondCurveEndPoint.set(
            (bottomNavView.mNavigationBarWidth / i) + (bottomNavView.CURVE_CIRCLE_RADIUS * 2) + (bottomNavView.CURVE_CIRCLE_RADIUS / 3),
            0
        )

        bottomNavView.mFirstCurveControlPoint1.set(
            bottomNavView.mFirstCurveStartPoint.x + bottomNavView.CURVE_CIRCLE_RADIUS + (bottomNavView.CURVE_CIRCLE_RADIUS / 4),
            bottomNavView.mFirstCurveStartPoint.y
        )

        bottomNavView.mFirstCurveControlPoint2.set(
            bottomNavView.mFirstCurveEndPoint.x - (bottomNavView.CURVE_CIRCLE_RADIUS * 2) + bottomNavView.CURVE_CIRCLE_RADIUS,
            bottomNavView.mFirstCurveEndPoint.y
        )


        bottomNavView.mSecondCurveControlPoint1.set(
            bottomNavView.mSecondCurveStartPoint.x + (bottomNavView.CURVE_CIRCLE_RADIUS * 2) -
                    bottomNavView.CURVE_CIRCLE_RADIUS, bottomNavView.mSecondCurveStartPoint.y
        )

        bottomNavView.mSecondCurveControlPoint2.set(
            bottomNavView.mSecondCurveEndPoint.x - (bottomNavView.CURVE_CIRCLE_RADIUS + (bottomNavView.CURVE_CIRCLE_RADIUS / 4)),
            bottomNavView.mSecondCurveEndPoint.y
        )
    }
}
