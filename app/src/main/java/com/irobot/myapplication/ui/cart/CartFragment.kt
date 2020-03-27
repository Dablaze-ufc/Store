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
import com.google.firebase.database.*
import com.irobot.myapplication.LoginActivity
import com.irobot.myapplication.R
import com.irobot.myapplication.data.CheckOutItems
import java.text.DecimalFormat

/**
 * A simple [Fragment] subclass.
 */
class CartFragment : Fragment() {
    private lateinit var adapter: ProductAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_cart, container, false)
        val button: MaterialButton = root.findViewById(R.id.check_out)
        val imageView: ImageView = root.findViewById(R.id.image_empty_cart)
        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_cart)
        val textTotal: TextView = root.findViewById(R.id.total_price)


        if (ShoppingCart.getCart().size == 0) {

            imageView.visibility = VISIBLE
        } else {
            imageView.visibility = GONE

            adapter = ProductAdapter(parentFragment!!.activity!!, ShoppingCart.getCart())
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

        }

        val formatter  = DecimalFormat("#,###,###")
        val price =  "₦${formatter.format(totalPrice())}"
        textTotal.text = price


        button.setOnClickListener {
            val auth = FirebaseAuth.getInstance().currentUser
            if (auth == null) {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            } else {
                var userPhoneNumber: String? = null
                val user = FirebaseAuth.getInstance().currentUser
                val databaseReference: DatabaseReference =
                    FirebaseDatabase.getInstance().getReference("cartItems")
                FirebaseDatabase.getInstance().getReference("users").child(user?.uid!!)
                    .addValueEventListener(object : ValueEventListener{
                             override fun onDataChange(dataSnapshot: DataSnapshot) {
                           userPhoneNumber = dataSnapshot.child("user_phoneNumber").value.toString()
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })
                   val id = databaseReference.push().key
                    if (userPhoneNumber != null){
                        val cartItem = id?.let {
                            CheckOutItems(ShoppingCart.getCart(), userPhoneNumber!!,user?.uid!!, textTotal.text.toString() )
                        }
                        databaseReference.child(id!!).setValue(cartItem).addOnCompleteListener{task->
                            if (task.isSuccessful){
                                Toast.makeText(
                                    requireContext(),
                                    "Your request will be processed shortly",
                                    Toast.LENGTH_SHORT
                                ).show()
                                ShoppingCart.clearCart()
                            }
                    }

                }else{
                        val cartItem =id?.let {
                            CheckOutItems(ShoppingCart.getCart(),user?.phoneNumber!!
                                ,user.uid
                                ,textTotal.text.toString())
                        }
                        databaseReference.child(id!!).setValue(cartItem).addOnCompleteListener{task->
                            if (task.isSuccessful){
                                Toast.makeText(
                                    requireContext(),
                                    "Your request will be processed shortly",
                                    Toast.LENGTH_SHORT
                                ).show()
                                ShoppingCart.clearCart()
                            }
                        }
                    }
                

            }
        }



        return root

    }
    private fun totalPrice() = ShoppingCart.getCart().fold(0.toDouble()) { acc, cartItem ->
        acc + cartItem
            .quantity.times(cartItem.product.price.toDouble())
    }

}
