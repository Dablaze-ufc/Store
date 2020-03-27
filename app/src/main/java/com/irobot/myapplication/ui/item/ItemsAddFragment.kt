package com.irobot.myapplication.ui.item


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.FoldingCube
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.irobot.myapplication.R
import com.irobot.myapplication.data.Items
import com.irobot.myapplication.databinding.FragmentItemsAddBinding
import java.io.ByteArrayOutputStream

/**
 * A simple [Fragment] subclass.
 */
class ItemsAddFragment : Fragment() {
    private lateinit var imageUrl: String
    private var bitmap: Bitmap? = null
    private var imagePath: String = ""
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
            showProgressBar()
            openGallery()
        }
        val foldingCube: Sprite = FoldingCube()
        binding.spinKit.setIndeterminateDrawable(foldingCube)
        binding.buttonItemAdd.setOnClickListener { _: View ->
            val name = binding.itemName.editText!!.text.toString().trim()
            val desc = binding.itemDescription.editText!!.text.toString().trim()
            var price = binding.itemPrice.editText!!.text.toString().trim()

            if (name.isNotEmpty() && desc.isNotEmpty() && price.isNotEmpty()) {
                showProgressBar()
                disableViews()
                uploadImageToFireBase()
            } else {
                enableViews()
                hideProgressBar()
                Toast.makeText(
                    requireContext(),
                    "Please fill in all the fields",
                    Toast.LENGTH_SHORT
                ).show()


            }

        }
        if (arguments != null) {

            binding.buttonItemAdd.text = "Save Item"
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 67 && resultCode == RESULT_OK) {
            val uri = data!!.data
            if (uri != null) {
                try {
                    if (Build.VERSION.SDK_INT >= 29) {
                        imagePath = uri.lastPathSegment!!
                        val imageDecoder =
                            ImageDecoder.createSource(requireActivity().contentResolver, uri)
                        bitmap = ImageDecoder.decodeBitmap(imageDecoder)
                        hideProgressBar()
                        Glide.with(requireActivity()).load(uri).into(binding.imageView)
                    } else {
                        imagePath = uri.lastPathSegment!!
                        bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            uri
                        )
                        hideProgressBar()
                        Glide.with(requireActivity()).load(uri).into(binding.imageView)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                hideProgressBar()
                Toast.makeText(parentFragment!!.context, "No image selected", Toast.LENGTH_SHORT)
                    .show()
            }

            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 67)
    }

    private fun uploadImageToFireBase() {
        if (bitmap != null) {
            val outputStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 40, outputStream)
            val data = outputStream.toByteArray()
            val firebaseStorage = FirebaseStorage.getInstance()
            val firebaseStorageReference: StorageReference =
                firebaseStorage.getReference("images/items/$imagePath")
            val task: UploadTask = firebaseStorageReference.putBytes(data)
            task.addOnCompleteListener { task1 ->
                if (task1.isSuccessful) {
                    task1.result!!.storage.downloadUrl.addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            imageUrl = task2.result.toString()
                            addItems()
                        }
                    }


                } else {
                    Toast.makeText(
                        parentFragment!!.context,
                        task1.exception!!.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            hideProgressBar()
            enableViews()
            Toast.makeText(parentFragment!!.context, "Please Select an Image", Toast.LENGTH_SHORT)
                .show()
        }

    }

    //    private fun validate(): Boolean{
//        if (binding.itemPrice.editText?.text!! == null) return false
//        if (binding.itemName.editText?.text!! == null) return false
//        if (binding.itemDescription.editText?.text!! == null) return false
//        return false
//    }
    private fun disableViews() {
        binding.itemName.isEnabled = false
        binding.itemDescription.isEnabled = false
        binding.itemPrice.isEnabled = false
    }

    private fun enableViews() {
        binding.itemName.isEnabled = true
        binding.itemDescription.isEnabled = true
        binding.itemPrice.isEnabled = true
    }

    private fun addItems() {
        val id = databaseReference.push().key
        val item = id?.let {
            Items(
                it,
                imageUrl,
                binding.itemName.editText!!.text.toString().trim(),
                binding.itemDescription.editText!!.text.toString().trim(),
                binding.itemPrice.editText!!.text.toString().trim()
            )
        }
        databaseReference.child(id!!).setValue(item).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                hideProgressBar()
                enableViews()
                Toast.makeText(parentFragment!!.context, "Success", Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireView()).navigate(R.id.itemsFragment)
            } else {
                hideProgressBar()
                enableViews()
                Toast.makeText(parentFragment!!.context, "oh oh", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showProgressBar() {
        binding.spinKit.visibility = VISIBLE
    }

    private fun hideProgressBar() {
        binding.spinKit.visibility = GONE
    }

}
