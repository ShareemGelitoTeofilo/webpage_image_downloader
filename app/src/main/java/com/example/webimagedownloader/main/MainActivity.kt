package com.example.webimagedownloader.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.webimagedownloader.R
import com.example.webimagedownloader.utils.Constants
import com.example.webimagedownloader.webscanning.WebsiteScanningActivity
import com.google.android.material.textfield.TextInputEditText
import androidx.lifecycle.ViewModelProvider
import com.example.webimagedownloader.utils.checkConnectivityAndExecute


class MainActivity : AppCompatActivity() {

    private lateinit var editTextDownloadDir: TextInputEditText
    private lateinit var viewModel: MainViewModel
    private lateinit var editTextSearchUrl: TextInputEditText

    private val setDownloadDirectory =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.data?.also { uri ->
                    viewModel.processDownloadDirectory(uri, applicationContext)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setObservers()

        viewModel.initDownloadUrl()

        editTextDownloadDir = findViewById(R.id.editTextDownloadFolder)
        editTextSearchUrl = findViewById(R.id.editTextUrl)

        findViewById<View>(R.id.btnSearch).setOnClickListener {
            checkConnectivityAndExecute(applicationContext) {
                val url = editTextSearchUrl.text.toString()
                viewModel.saveSearchUrlToPreference(url)
                openWebsiteScannerScreen()
            }
        }

        findViewById<View>(R.id.editTextDownloadFolder).setOnClickListener {
            val i = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            i.addCategory(Intent.CATEGORY_DEFAULT)
            setDownloadDirectory.launch(Intent.createChooser(i, "Choose directory"))
        }
    }

    private fun setObservers() {
        viewModel.downloadPath.observe(this) {
            editTextDownloadDir.setText(it)
        }
        viewModel.searchUrl.observe(this) {
            editTextSearchUrl.setText(it)
        }
    }

    private fun openWebsiteScannerScreen() {
        val intent = Intent(this, WebsiteScanningActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.setSearchUrlFromPreference()
    }
}