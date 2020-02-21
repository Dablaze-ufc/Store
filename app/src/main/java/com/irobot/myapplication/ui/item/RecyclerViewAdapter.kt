package com.irobot.myapplication.ui.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.irobot.myapplication.R
import com.irobot.myapplication.data.Items
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
            val (imageUrl, tittle, description, price) = getItem(position)
            Glide.with(mContext).load(imageUrl).into(holder.imgUser)
            holder.itemTxtTitle.text = tittle
            holder.itemTxtMessage.text = description
            holder.itemTxtPrice.text = price
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

        init {
            // ButterKnife.bind(this, itemView);
            itemView.setOnClickListener {
                mItemClickListener!!.onItemClick(
                    itemView,
                    adapterPosition,
                    modelList[adapterPosition]
                )
            }
        }
    }

}