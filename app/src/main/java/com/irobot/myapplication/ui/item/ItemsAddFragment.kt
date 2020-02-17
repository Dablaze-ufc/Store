package com.irobot.myapplication.ui.item


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton

import com.irobot.myapplication.R
import kotlinx.android.synthetic.main.fragment_items_add.*

/**
 * A simple [Fragment] subclass.
 */
class ItemsAddFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_items_add, container, false)
        if (arguments != null) {
            var button = root.findViewById<MaterialButton>(R.id.button_item_add)
            button.text = "Save Item"
        }
        return root
    }


}
