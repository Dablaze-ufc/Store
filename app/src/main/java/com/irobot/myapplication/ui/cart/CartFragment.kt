package com.irobot.myapplication.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.irobot.myapplication.LoginActivity
import com.irobot.myapplication.R

/**
 * A simple [Fragment] subclass.
 */
class CartFragment : Fragment() {
    lateinit var adapter: ProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_cart, container, false)
        val button: MaterialButton = root.findViewById(R.id.check_out)
        val imageView: ImageView = root.findViewById(R.id.image_empty_cart)
        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_cart)


        if (ShoppingCart.getCart().size == 0) {

            imageView.visibility = VISIBLE
        } else {
            imageView.visibility = GONE
            adapter = ProductAdapter(parentFragment!!.activity!!, ShoppingCart.getCart())
            adapter.notifyDataSetChanged()
            adapter.hasStableIds()
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

        }


        button.setOnClickListener { v ->
            val auth = FirebaseAuth.getInstance().currentUser
            if (auth == null) {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Your items will be delivered shortly",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        val totalPrice = ShoppingCart.getCart().fold(0.toDouble()) { acc, cartItem ->
            acc + cartItem
                .quantity.times(cartItem.product.price.toDouble())
        }
        val textTotal: TextView = root.findViewById(R.id.total_price)
        textTotal.text = """₦${totalPrice}"""
        return root

    }

}
