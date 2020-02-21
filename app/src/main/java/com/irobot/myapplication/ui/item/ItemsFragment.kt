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
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*

import com.irobot.myapplication.R
import com.irobot.myapplication.data.Items

/**
 * A simple [Fragment] subclass.
 */
class ItemsFragment : Fragment() {

    private var items: List<Items> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_items, container, false)
        recyclerView = root.findViewById(R.id.recycle)
        val button = root.findViewById<MaterialButton>(R.id.add_button)
        shimmerFrameLayout = root.findViewById(R.id.shimmer_recycler_view)
        shimmerFrameLayout.startShimmer()
        button.setOnClickListener { v ->
            Navigation.findNavController(v).navigate(R.id.action_itemsFragment_to_itemsAddFragment)
        }
        setUpRecyclerView()
        return root
    }

    private fun setUpRecyclerView() {
        val gridLayoutManager = GridLayoutManager(parentFragment!!.context, 2)
        recyclerView.addItemDecoration(
            GridMarginDecoration(
                activity,
                2,
                2,
                2,
                2
            )
        )
        getItems()
        val adapter = ItemsRecyclerAdapter(items)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = gridLayoutManager
        shimmerFrameLayout.stopShimmer()
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
