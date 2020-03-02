package com.irobot.myapplication.ui.item


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*
import com.irobot.myapplication.R
import com.irobot.myapplication.data.Items

/**
 * A simple [Fragment] subclass.
 */
class ItemsFragment : Fragment() {

    private var items: ArrayList<Items>? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_items, container, false)
        recyclerView = root.findViewById(R.id.recycle)
        val button = root.findViewById<MaterialButton>(R.id.add_button)
        val swipeRefreshLayout = root.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(
                parentFragment!!.context!!,
                R.color.colorAccent
            )
        )
        swipeRefreshLayout.isRefreshing = true
//        setSupportActionBar(toolbar)
        button.setOnClickListener { v ->

            Navigation.findNavController(parentFragment!!.activity!!, R.id.fragment)
                .navigate(R.id.action_itemsFragment_to_itemsAddFragment)
        }
//        setUpRecyclerView()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val gridLayoutManager = GridLayoutManager(activity, 2)
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
        val adapter = RecyclerViewAdapter(parentFragment!!.activity!!, items!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = gridLayoutManager
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
                items!!.clear()
                for (snapshot in dataSnapshot.children) {
                    val item: Items = snapshot.getValue(Items::class.java)!!
                    items!!.add(item)
                }

            }

        })
    }


}
