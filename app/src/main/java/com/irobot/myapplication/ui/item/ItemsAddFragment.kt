package com.irobot.myapplication.ui.item


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

import com.irobot.myapplication.R
import com.irobot.myapplication.data.Items
import com.irobot.myapplication.databinding.FragmentItemsAddBinding
import kotlinx.android.synthetic.main.fragment_items_add.*
import java.io.ByteArrayOutputStream

/**
 * A simple [Fragment] subclass.
 */
class ItemsAddFragment : Fragment() {
    private lateinit var imageUrl: String
    private lateinit var bitmap: Bitmap
    private lateinit var binding: FragmentItemsAddBinding
    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("items")



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_items_add, container, false)
        binding.imageView.setOnClickListener { v ->
            openGallery()
        }
        binding.buttonItemAdd.setOnClickListener { v: View ->
            addItems()
        }
        if (arguments != null) {

            binding.buttonItemAdd.text = "Save Item"
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 67 && resultCode == RESULT_OK) {
            var uri = data!!.data
            if (uri != null) {
                try {
                    if (Build.VERSION.SDK_INT >= 29) {
                        var imageDecoder =
                            ImageDecoder.createSource(requireActivity().contentResolver, uri)
                        bitmap = ImageDecoder.decodeBitmap(imageDecoder)
                    } else {
                        bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            uri
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(parentFragment!!.context, "No image", Toast.LENGTH_SHORT).show()
            }

            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivity(intent)
    }

    private fun uploadImageToFireBase() {
        var outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream)
        var data = outputStream.toByteArray()
        var firebaseStorage = FirebaseStorage.getInstance().getReference("images/items")
        var task: UploadTask = firebaseStorage.putBytes(data)
        task.addOnCompleteListener { task1 ->
            if (task1.isSuccessful) {
                task1.result!!.storage.downloadUrl.addOnCompleteListener { task2 ->
                    if (task2.isSuccessful) {
                        imageUrl = task2.result.toString()

                    }
                }


            } else {
                Toast.makeText(
                    parentFragment!!.context,
                    "Success Image upload error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addItems() {
        var id = databaseReference.push().key
        imageUrl = ""
        val item = Items(
            imageUrl,
            binding.itemName.editText!!.text.toString().trim(),
            binding.itemDescription.editText!!.text.toString().trim(),
            binding.itemPrice.editText!!.text.toString().trim()
        )
        databaseReference.child(id!!).setValue(item).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(parentFragment!!.context, "Success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(parentFragment!!.context, "oh oh", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
