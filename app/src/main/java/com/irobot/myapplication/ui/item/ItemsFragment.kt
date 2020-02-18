package com.irobot.myapplication.ui.item


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

import com.irobot.myapplication.R
import com.irobot.myapplication.data.Items

/**
 * A simple [Fragment] subclass.
 */
class ItemsFragment : Fragment() {

    var items: List<Items>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_items, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.recycle)
        val button = root.findViewById<MaterialButton>(R.id.add_button)
        button.setOnClickListener { v ->
            Navigation.findNavController(v).navigate(R.id.action_itemsFragment_to_itemsAddFragment)
        }
        val gridLayoutManager = GridLayoutManager(parentFragment!!.context, 2)
        val adapter = ItemsRecyclerAdapter(items)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = gridLayoutManager
        return root
    }


}
