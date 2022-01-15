package com.example.webimagedownloader.webscanning

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.webimagedownloader.R
import com.example.webimagedownloader.utils.Constants
import com.example.webimagedownloader.utils.checkConnectivityAndExecute
import com.example.webimagedownloader.webscanning.dialog.ScanningCompleteDialog
import com.example.webimagedownloader.webscanning.dialog.ScanningProgressDialog
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.view.inputmethod.EditorInfo

import android.widget.TextView
import android.widget.TextView.OnEditorActionListener


@AndroidEntryPoint
open class WebsiteScanningActivity : AppCompatActivity() {

    // TODO add validation of URL

    private lateinit var webView: WebView
    private var url: String? = "https://google.com"
    private lateinit var scanningProgressDialog: ScanningProgressDialog
    private val scrapedImages = mutableListOf<String>()
    private lateinit var scanningHandler: ScanningHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_website_scanning)
        scanningProgressDialog = ScanningProgressDialog()
        intent?.extras?.let {
           url = it.getString(Constants.URL)
            //url = it.getString(Constants.URL)
        }

        setScanningHandler(object: ScanningHandler {
            override fun onEnd() {
                onStatusEndScanning()
            }
        })

        val viewModel = ViewModelProvider(this)[WebScanningViewModel::class.java]
        initObservers(viewModel)


        val editTextSearch = findViewById<TextInputEditText>(R.id.editTextUrl)

        url?.let { url ->
            displayWebsite(url)
            findViewById<Button>(R.id.btnWebsiteScan).setOnClickListener {
                checkConnectivityAndExecute(applicationContext) {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.onScanBtnClicked(url)
                    }
                }
            }
            editTextSearch.setText(url)
        }

        editTextSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                displayWebsite(editTextSearch.text.toString())
                true
            } else false
        }
    }

    fun setScanningHandler(scanningHandler: ScanningHandler) {
        this.scanningHandler = scanningHandler
    }

    private fun initObservers(viewModel: WebScanningViewModel) {
        viewModel.scanningStatus.observe(this) {
            when (it) {
                WebScanningViewModel.STATUS_STARTED -> {
                    scanningProgressDialog.show(supportFragmentManager, "scanning_progress_dialog")
                }
                WebScanningViewModel.STATUS_ENDED -> {
                    // onStatusEndScanning()
                    scanningHandler.onEnd()
                }
            }
        }

        viewModel.scrapedImages.observe(this) {
            scrapedImages.clear()
            scrapedImages.addAll(it)
        }
    }

    private fun onStatusEndScanning() {
        if (scanningProgressDialog.isVisible) {
            scanningProgressDialog.dismiss()
        }
        onScanningCompleted(scrapedImages)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun displayWebsite(url: String) {
        webView = findViewById(R.id.webViewToScan)
        webView.webViewClient = WebViewClient()
        // this will load the url of the website
        webView.loadUrl(url)
        // this will enable the javascript settings
        webView.settings.javaScriptEnabled = true
        // if you want to enable zoom feature
        webView.settings.setSupportZoom(true)
    }

    override fun onBackPressed() {
        if (webView.canGoBack())
            webView.goBack()
        else
            super.onBackPressed()
    }

    open fun onScanningCompleted(scrapedImgUrls: List<String>) {
        val scanningCompleteDialog = ScanningCompleteDialog(scrapedImgUrls)
        scanningCompleteDialog.show(supportFragmentManager, "scanning_completed_dialog")
    }
}