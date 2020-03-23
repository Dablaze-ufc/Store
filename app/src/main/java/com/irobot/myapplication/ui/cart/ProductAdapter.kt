package com.irobot.myapplication.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.irobot.myapplication.R
import com.irobot.myapplication.data.CartItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_recycler_items.view.*
import java.text.NumberFormat
import java.util.*

class ProductAdapter(var context: Context, var cartItem: List<CartItem>) :
    RecyclerView.Adapter<ProductAdapter.ShoppingCartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartViewHolder {
        val layout =
            LayoutInflater.from(context).inflate(R.layout.cart_recycler_items, parent, false)
        return ShoppingCartViewHolder(layout)
    }

    override fun getItemCount(): Int = cartItem.size

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        holder.bindItem(cartItem[position])
        holder.itemView.findViewById<ImageButton>(R.id.removeFromCart).setOnClickListener{v ->
            ShoppingCart.removeItem(cartItem[position],context)
        }
    }

    class ShoppingCartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(cartItem: CartItem) {
            itemView.text_price.text = "₦${formantNumber(cartItem.product.price)}"
            itemView.text_quantity.text = cartItem.quantity.toString()
            itemView.text_tittle.text = cartItem.product.tittle
            Picasso.get().load(cartItem.product.imageUrl).fit().into(itemView.item_imageView)
        }

    }
    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position



}
fun formantNumber(number:String): String{
    return if(number.isNotEmpty()){
        val number2 = number.toDouble()
        NumberFormat.getNumberInstance(Locale.US).format(number2)

    }else
        "0"
}