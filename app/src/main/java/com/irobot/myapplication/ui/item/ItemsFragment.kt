package com.irobot.myapplication.ui.item


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.irobot.myapplication.R
import com.irobot.myapplication.data.Items

/**
 * A simple [Fragment] subclass.
 */
class ItemsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerContext: Context
    private lateinit var emptyStore: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_items, container, false)
        recyclerView = root.findViewById(R.id.recycle)
        val button = root.findViewById<MaterialButton>(R.id.add_button)
        emptyStore = root.findViewById(R.id.image_empty_store)
        button.setOnClickListener { v ->

            Navigation.findNavController(parentFragment!!.activity!!, R.id.fragment)
                .navigate(R.id.action_itemsFragment_to_itemsAddFragment)
        }
//        setUpRecyclerView()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.addItemDecoration(
            GridMarginDecoration(
                activity,
                2,
                2,
                2,
                2
            )
        )
        var items: ArrayList<Items> = arrayListOf()
        val database = FirebaseDatabase.getInstance().getReference("items")
        items.clear()
        database.addValueEventListener(object : ValueEventListener {


            override fun onDataChange(dataSnapshot: DataSnapshot) {
                items.clear()
                for (snapshot in dataSnapshot.children) {
                    val item: Items = snapshot.getValue(Items::class.java)!!
                    items.add(item)
                }

                if (items.size == 0) {
                    emptyStore.visibility = VISIBLE
                } else {
                    emptyStore.visibility = GONE
                    val adapter = RecyclerViewAdapter(recyclerContext, items)
                    Log.d("no events", "find Events" + items.toString())
                    adapter.updateList(items)
                    adapter.setHasStableIds(true)
                    recyclerView.adapter = adapter
                }

            }


            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(parentFragment!!.context, "error loading list", Toast.LENGTH_SHORT)
                    .show()
            }
        })



    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        recyclerContext = context

    }



    }



