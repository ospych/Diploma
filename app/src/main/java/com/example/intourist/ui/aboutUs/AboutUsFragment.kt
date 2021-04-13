package com.example.intourist.ui.aboutUs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.intourist.R
import kotlinx.android.synthetic.main.fragment_about_us.*

class AboutUsFragment : Fragment(R.layout.fragment_about_us) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.show()

        gettingChat.setOnClickListener {
            if (startChat.visibility == View.GONE) {
                startChat.visibility = View.VISIBLE
                down.visibility = View.GONE
                up.visibility = View.VISIBLE
            } else {
                startChat.visibility = View.GONE
                down.visibility = View.VISIBLE
                up.visibility = View.GONE
            }

        }

        startChatButton.setOnClickListener {
            val url = "https://api.whatsapp.com/send/?phone=996999169299&text&app_absent=0"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            val chooser = Intent.createChooser(intent, "Chrome")
            startActivity(chooser)
        }

        aboutUs.setOnClickListener {
            if (scrollView.visibility == View.GONE) {
                scrollView.visibility = View.VISIBLE
                down2.visibility = View.GONE
                up2.visibility = View.VISIBLE
            } else {
                scrollView.visibility = View.GONE
                down2.visibility = View.VISIBLE
                up2.visibility = View.GONE
            }
        }
    }
}