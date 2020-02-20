package com.irobot.myapplication.ui.item


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*

import com.irobot.myapplication.R
import com.irobot.myapplication.data.Items

/**
 * A simple [Fragment] subclass.
 */
class ItemsFragment : Fragment() {

    private var items: List<Items> = ArrayList()

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
        getItems()
        val adapter = ItemsRecyclerAdapter(items)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = gridLayoutManager
        return root
    }

    private fun getItems() {
        val database: Query = FirebaseDatabase.getInstance().getReference("items")
            .orderByKey()

        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(parentFragment!!.context, "error loading list", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                (items as ArrayList?)!!.clear()
                for (snapshot in dataSnapshot.children) {
                    val item: Items = snapshot.getValue(Items::class.java)!!
                    (items as ArrayList).add(item)
                }

            }

        })
    }


}
