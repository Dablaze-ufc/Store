package com.irobot.myapplication.ui.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.irobot.myapplication.MainActivity
import com.irobot.myapplication.R
import com.irobot.myapplication.data.CartItem
import com.irobot.myapplication.data.Items
import com.irobot.myapplication.ui.cart.ShoppingCart
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat
import java.util.*

/**
 * A custom adapter to use with the RecyclerView widget.
 */
class RecyclerViewAdapter(
    private val mContext: Context,
    private var modelList: ArrayList<Items>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mItemClickListener: OnItemClickListener? =
        null

    fun updateList(modelList: ArrayList<Items>) {
        this.modelList = modelList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_recycler_list, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) { //Here you can fill your row view
        if (holder is ViewHolder) {
            val (_, imageUrl, tittle, description, price) = getItem(position)
            Glide.with(mContext).load(imageUrl).into(holder.imgUser)
            holder.itemTxtTitle.text = tittle
            holder.itemTxtMessage.text = description

            holder.itemTxtPrice.text = "₦${formantNumber(price)}"
            Observable.create(ObservableOnSubscribe<MutableList<CartItem>> {
                holder.addButton.setOnClickListener { view ->
                    val item = CartItem(getItem(position))
                    ShoppingCart.addItem(item)

                    Snackbar.make(
                        (holder.itemView.context as MainActivity).constraint,
                        "${item.product.tittle} added to your cart", Snackbar.LENGTH_LONG
                    ).show()

                    it.onNext(ShoppingCart.getCart())

                }
            }).subscribe { cart ->
                var quantity = 0
                cart.forEach { cartItem ->
                    quantity += cartItem.quantity
                }
                (holder.itemView.context as MainActivity).bottomNavBar.setCount(
                    2,
                    quantity.toString()
                )
                Toast.makeText(holder.itemView.context, "Cart size $quantity", Toast.LENGTH_SHORT)
                    .show()
            }


        }
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener?) {
        this.mItemClickListener = mItemClickListener
    }

    private fun getItem(position: Int): Items {
        return modelList[position]
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, model: Items?)
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imgUser: ImageView = itemView.findViewById<View>(R.id.img_user) as ImageView
        val itemTxtTitle: TextView = itemView.findViewById<View>(R.id.item_txt_title) as TextView
        val itemTxtMessage: TextView =
            itemView.findViewById<View>(R.id.item_txt_message) as TextView
        val itemTxtPrice: TextView = itemView.findViewById<View>(R.id.item_txt_price) as TextView
        val addButton = itemView.findViewById<ImageButton>(R.id.add_to_cart)

        init {
            // ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener {
//                mItemClickListener!!.onItemClick(
//                    itemView,
//                    adapterPosition,
//                    modelList[adapterPosition]
//                )
//            }
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