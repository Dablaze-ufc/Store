package com.irobot.myapplication.ui.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.irobot.myapplication.R
import com.irobot.myapplication.data.Items

class ItemsRecyclerAdapter(private var itemsList: List<Items>) :
    RecyclerView.Adapter<ItemsRecyclerAdapter.ItemsRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsRecyclerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemsRecyclerViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(holder: ItemsRecyclerViewHolder, position: Int) {
        var item: Items = itemsList.get(position)
        holder.bind(item)
        holder.itemView.findViewById<MaterialCardView>(R.id.item_single)
            .setOnClickListener { v: View ->
                Navigation.findNavController(v).navigate()
            }
    }

    class ItemsRecyclerViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(
                R.layout.items_list, parent, false
            )
        ) {
        var itemImage: ImageView? = null
        var itemTittle: TextView? = null
        var itemDescription: TextView? = null
        var itemPrice: TextView? = null

        init {
            itemImage = itemView.findViewById(R.id.item_image)
            itemTittle = itemView.findViewById(R.id.item_tittle)
            itemDescription = itemView.findViewById(R.id.item_descp)
            itemPrice = itemView.findViewById(R.id.item_price)
        }

        fun bind(item: Items) {
            Glide.with(itemView).load(item.imageUrl).into(itemImage!!)
            itemTittle!!.text = item.tittle
            itemDescription!!.text = item.description
            itemPrice!!.text = item.price
        }

    }


}