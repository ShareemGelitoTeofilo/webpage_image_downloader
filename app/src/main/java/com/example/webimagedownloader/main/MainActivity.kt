package com.example.webimagedownloader.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.webimagedownloader.R
import com.example.webimagedownloader.utils.Constants
import com.example.webimagedownloader.webscanning.WebsiteScanningActivity
import com.google.android.material.textfield.TextInputEditText
import java.io.File


class MainActivity : AppCompatActivity() {

    private val MY_REQUEST_CODE = 123213
    val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            it.data?.data?.also { uri ->
                // Perform operations on the document using its URI.
                /*val file = File(Uri(uri.path))
                Toast.makeText(applicationContext, file.toString(), Toast.LENGTH_SHORT).show()*/
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btnSearch).setOnClickListener {
            val url = findViewById<TextInputEditText>(R.id.editTextUrl).text.toString()
            openWebsiteScannerScreen(url)
        }

        findViewById<View>(R.id.editTextDownloadFolder).setOnClickListener {

            val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            i.addCategory(Intent.CATEGORY_DEFAULT)
            getContent.launch(Intent.createChooser(i, "Choose directory"))

            /*val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            i.addCategory(Intent.CATEGORY_DEFAULT)

            startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999)*/
        }
    }

    private fun openWebsiteScannerScreen(url: String) {
        val intent = Intent(this, WebsiteScanningActivity::class.java).apply {
            putExtra(Constants.URL, url)
        }
        startActivity(intent)
    }
}