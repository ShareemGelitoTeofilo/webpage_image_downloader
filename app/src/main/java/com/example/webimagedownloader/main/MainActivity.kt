package com.example.webimagedownloader.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.webimagedownloader.R
import com.example.webimagedownloader.utils.Constants
import com.example.webimagedownloader.utils.SharedPreferenceHelper
import com.example.webimagedownloader.webscanning.WebsiteScanningActivity
import com.google.android.material.textfield.TextInputEditText
import android.provider.MediaStore
import android.provider.DocumentsContract
import com.example.webimagedownloader.utils.UriUtils





class MainActivity : AppCompatActivity() {

    private lateinit var editTextDownloadDir: TextInputEditText

    private val setDownloadDirectory =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                // The result data contains a URI for the document or directory that
                // the user selected.
                it.data?.data?.also { uri ->
                    // TODO move later on the view models
                    val docUriTree = DocumentsContract.buildDocumentUriUsingTree(
                        uri,
                        DocumentsContract.getTreeDocumentId(uri)
                    )
                    val fullFilePath = UriUtils.getPathFromUri(this, docUriTree)
                    SharedPreferenceHelper.addString(Constants.URL, fullFilePath)
                    val path = SharedPreferenceHelper.getString(Constants.URL, "")

                    editTextDownloadDir.setText(path)
                }
            }

        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editTextDownloadDir = findViewById(R.id.editTextDownloadFolder)
        findViewById<View>(R.id.btnSearch).setOnClickListener {
            val url = findViewById<TextInputEditText>(R.id.editTextUrl).text.toString()
            openWebsiteScannerScreen(url)
        }
        findViewById<View>(R.id.editTextDownloadFolder).setOnClickListener {
            val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            i.addCategory(Intent.CATEGORY_DEFAULT)
            setDownloadDirectory.launch(Intent.createChooser(i, "Choose directory"))
        }
    }

    private fun openWebsiteScannerScreen(url: String) {
        val intent = Intent(this, WebsiteScanningActivity::class.java).apply {
            putExtra(Constants.URL, url)
        }
        startActivity(intent)
    }
}