package com.irobot.myapplication.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.irobot.myapplication.R
import com.irobot.myapplication.data.CartItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_recycler_items.view.*

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
    }

    class ShoppingCartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(cartItem: CartItem) {
            itemView.text_price.text = cartItem.product.price
            itemView.text_quantity.text = cartItem.quantity.toString()
            itemView.text_tittle.text = cartItem.product.tittle
            Picasso.get().load(cartItem.product.imageUrl).fit().into(itemView.item_imageView)
        }

    }


}