package com.irobot.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.etebarian.meowbottomnavigation.MeowBottomNavigation

class MainActivity : AppCompatActivity() {
    companion object {
        private const val ID_STORE = 1
        private const val ID_CART = 2
        private const val ID_PROFILE = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val curvedBottomNavigationView = findViewById<MeowBottomNavigation>(R.id.bottomNavBar)

        curvedBottomNavigationView.show(ID_STORE)
        curvedBottomNavigationView.add(
            MeowBottomNavigation.Model(
                ID_CART,
                R.drawable.ic_local_grocery_store_black_24dp
            )
        )
        curvedBottomNavigationView.add(
            MeowBottomNavigation.Model(
                ID_STORE,
                R.drawable.ic_store_black_24dp
            )
        )
        curvedBottomNavigationView.add(
            MeowBottomNavigation.Model(
                ID_PROFILE,
                R.drawable.ic_account_supervisor
            )
        )

        curvedBottomNavigationView.setCount(ID_STORE, "3")

        curvedBottomNavigationView.setOnClickMenuListener {

            val fragmentTransaction = this.supportFragmentManager.beginTransaction()
            when (it.id) {
                ID_CART -> Navigation.findNavController(
                    this,
                    R.id.fragment
                ).navigate(R.id.cartFragment)
                ID_PROFILE -> Navigation.findNavController(
                    this,
                    R.id.fragment
                ).navigate(R.id.profileFragment)
                else -> Navigation.findNavController(
                    this,
                    R.id.fragment
                ).navigate(R.id.itemsFragment)
            }
        }
    }


}
