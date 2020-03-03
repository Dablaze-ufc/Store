package com.irobot.myapplication.ui.cart

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.irobot.myapplication.data.CartItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_recycler_items.view.*

class ProductAdapter(var context: Context, var cartItem: CartItem) :
    RecyclerView.Adapter<ProductAdapter.ShoppingCartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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