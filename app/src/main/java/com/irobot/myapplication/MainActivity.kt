package com.irobot.myapplication

import android.content.DialogInterface
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.paperdb.Paper

class MainActivity : AppCompatActivity() {
    companion object {

        private const val ID_STORE = 1
        private const val ID_CART = 2
        private const val ID_PROFILE = 3

    }

    private lateinit var toolBar: MaterialToolbar

    private lateinit var mNavController: NavController

    private lateinit var curvedBottomNavigationView: MeowBottomNavigation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Paper.init(this)

        curvedBottomNavigationView = findViewById(R.id.bottomNavBar)
        mNavController = Navigation.findNavController(this, R.id.fragment)
        toolBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)
        initDestinationListener()
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

//        curvedBottomNavigationView.setCount(ID_CART, ShoppingCart.getShoppingCartSize().toString())

        curvedBottomNavigationView.setOnClickMenuListener {
            when (it.id) {
                ID_CART -> {
                    mNavController.navigate(R.id.cartFragment)
                    curvedBottomNavigationView.clearCount(ID_CART)
                }
                ID_PROFILE -> mNavController.navigate(R.id.profileFragment)
                else -> mNavController.navigate(R.id.itemsFragment)
            }
        }

        curvedBottomNavigationView.setOnShowListener {
            when(it.id){
               ID_CART ->  {R.id.cartFragment
//                            curvedBottomNavigationView.show(ID_CART)
                            }
                 ID_PROFILE-> R.id.profileFragment
                else -> R.id.profileFragment
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return mNavController.navigateUp()|| super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (mNavController.currentDestination!!.id == R.id.itemsFragment)
            showDialog()
        else
            mNavController.navigateUp()
    }

    private fun showDialog() {
      val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("Exit!")
            .setPositiveButton("Yes") {
                dialog: DialogInterface?, i: Int ->
                dialog?.dismiss()
                System.exit(0)
            }
            .setNegativeButton("No") { dialog: DialogInterface?, i: Int ->
                dialog?.dismiss()
            }
        dialog.create().show()


    }

    private fun initDestinationListener() {
        mNavController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.itemsAddFragment -> hideBottomNav()
                else -> showBottomNav()

            }
        }
    }

    private fun hideBottomNav() {
        curvedBottomNavigationView.visibility = GONE
    }

    private fun showBottomNav() {
        curvedBottomNavigationView.visibility = VISIBLE
    }

    private fun hideCustomToolBar() {
        toolBar.visibility = GONE
    }

    private fun showCustomToolBar() {
        toolBar.visibility = VISIBLE
    }


}
