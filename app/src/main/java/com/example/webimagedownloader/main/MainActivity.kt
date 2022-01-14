package com.example.webimagedownloader.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.webimagedownloader.R
import com.example.webimagedownloader.utils.Constants
import com.example.webimagedownloader.webscanning.WebsiteScanningActivity
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btnSearch).setOnClickListener {
            val url = findViewById<TextInputEditText>(R.id.editTextUrl).text.toString()
            openWebsiteScannerScreen(url)
        }
    }

    private fun openWebsiteScannerScreen(url: String) {
        val intent = Intent(this, WebsiteScanningActivity::class.java).apply {
            putExtra(Constants.URL, url)
        }
        startActivity(intent)
    }
}