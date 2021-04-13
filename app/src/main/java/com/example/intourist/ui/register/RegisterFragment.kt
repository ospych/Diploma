package com.example.intourist.ui.register

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.intourist.R
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment(R.layout.fragment_register) {

    companion object {
        val SELECT_IMAGE_CODE = 100
    }
    private lateinit var imageReceipt: Uri

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.title = "Регистрация"
        toolbar?.show()

        val titleTour = arguments?.getString("TourTitle")
        Log.v("Title", titleTour.toString())

        titleRegister.text = "Название тура: $titleTour"

        receiptButton.setOnClickListener {
            addImage()
        }

        registerButton.setOnClickListener {
            val name = nameET.text.toString()
            val phoneNumber = phoneET.text.toString()
            val email = "gitpych@gmail.com"
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Регистрация на тур")
            emailIntent.putExtra(
                Intent.EXTRA_TEXT, "Имя: $name " +
                    "\nТелефонный номер: $phoneNumber\nНазвание тура: $titleTour")
            emailIntent.putExtra(Intent.EXTRA_STREAM, imageReceipt)
            emailIntent.type = "plain/text"
            val chooser = Intent.createChooser(emailIntent, "Выберите Gmail")
            startActivity(chooser)
        }
    }

    private fun addImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra("return-data", true)
        startActivityForResult(Intent.createChooser(intent, "Select image"), SELECT_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == SELECT_IMAGE_CODE
            && resultCode == Activity.RESULT_OK && data != null) {

            imageReceipt = data.data!!
            try {
                imageReceipt.let {
                    receipt.setImageURI(imageReceipt)
                    receipt.visibility = View.VISIBLE
                    registerButton.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

}