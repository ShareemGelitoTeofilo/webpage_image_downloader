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
import java.io.File
import android.provider.MediaStore
import android.provider.DocumentsContract
import com.example.webimagedownloader.utils.FileHelper
import com.example.webimagedownloader.utils.UriUtils





class MainActivity : AppCompatActivity() {

    private val getDirectory =
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
                    // val realPath = FileHelper.getUriRealPath(this, uri) ?: ""

                    SharedPreferenceHelper.addString(Constants.URL, fullFilePath)

                    val path = SharedPreferenceHelper.getString(Constants.URL, "")
                    Toast.makeText(applicationContext, path, Toast.LENGTH_SHORT).show()
                }
            }

        }

    fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        contentUri?.let {
            return try {
                val proj = arrayOf(MediaStore.Images.Media.DATA)
                cursor = context.contentResolver.query(contentUri, proj, null, null, null)
                val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor!!.moveToFirst()
                cursor!!.getString(column_index)
            } finally {
                cursor?.close()
            }
        }
        return null
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
            getDirectory.launch(Intent.createChooser(i, "Choose directory"))
        }
    }

    private fun openWebsiteScannerScreen(url: String) {
        val intent = Intent(this, WebsiteScanningActivity::class.java).apply {
            putExtra(Constants.URL, url)
        }
        startActivity(intent)
    }
}