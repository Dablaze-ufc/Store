package com.irobot.myapplication.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.irobot.myapplication.R


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        val textSignOut = root.findViewById<TextView>(R.id.text_signOut)

        val userEmail = root.findViewById<TextView>(R.id.text_email)
        val userName = root.findViewById<TextView>(R.id.text_profile_name)

        textSignOut.setOnClickListener { v ->
            val auth = FirebaseAuth.getInstance()
            if (auth != null){
                Toast.makeText(requireContext(), "SignOut successFully", Toast.LENGTH_SHORT).show()
                auth.signOut()
            }else{
                Snackbar.make(
                        root.findViewById(R.id.constraintLayout),
                        "Please Sign In", Snackbar.LENGTH_LONG
                    )
                    .setAction(
                        "Sign In"
                    ) { v1: View? ->
                        Navigation.findNavController(
                            textSignOut
                        ).navigate(R.id.loginFragment)
                    }.show()

            }

        }

        val user = FirebaseAuth.getInstance().currentUser
            if (user == null){
                userEmail.visibility = GONE
                userName.visibility = GONE
            }else{
            userEmail.text =  user.email
            userName.text =user.email
            }


        return root
    }

}
