package com.example.webimagedownloader.webscanning

import android.annotation.SuppressLint
import android.media.Image
import android.os.Bundle
import android.view.Menu
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
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
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import androidx.appcompat.app.ActionBar

import androidx.lifecycle.MutableLiveData


@AndroidEntryPoint
open class WebsiteScanningActivity : AppCompatActivity() {

    // TODO add validation of URL

    private lateinit var webView: WebView
    private var searchUrl = MutableLiveData("https://google.com")
    private lateinit var scanningProgressDialog: ScanningProgressDialog
    private val scrapedImages = mutableListOf<String>()
    private lateinit var scanningHandler: ScanningHandler
    private lateinit var editTextSearch: TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_website_scanning)

        supportActionBar?.setTitle(R.string.web_scanner)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        scanningProgressDialog = ScanningProgressDialog()
        intent?.extras?.let {
           searchUrl.value = it.getString(Constants.URL)
            //url = it.getString(Constants.URL)
        }

        setScanningHandler(object: ScanningHandler {
            override fun onEnd() {
                onStatusEndScanning()
            }
        })

        val viewModel = ViewModelProvider(this)[WebScanningViewModel::class.java]
        initObservers(viewModel)


        editTextSearch = findViewById(R.id.editTextUrl)

        searchUrl.value?.let { url ->
            displayWebsite(url)
            editTextSearch.setText(url)
        }

        findViewById<Button>(R.id.btnWebsiteScan).setOnClickListener {
            checkConnectivityAndExecute(applicationContext) {
                CoroutineScope(Dispatchers.IO).launch {
                    searchUrl.value?.let { url ->
                        viewModel.onScanBtnClicked(url)
                    }
                }
            }
        }

        findViewById<ImageView>(R.id.imgClearSearch).setOnClickListener {
            editTextSearch.setText("")
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

        searchUrl.observe(this) {
            editTextSearch.setText(it)
        }
    }

    private fun onStatusEndScanning() {
        if (scanningProgressDialog.isVisible) {
            scanningProgressDialog.dismiss()
        }
        onScanningCompleted(scrapedImages)
    }

    private fun setUrl(url: String) {
        searchUrl.value = url
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun displayWebsite(url: String) {
        webView = findViewById(R.id.webViewToScan)
        webView.webViewClient = object: WebViewClient() {
            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                url?.let { setUrl(it) }
                super.doUpdateVisitedHistory(view, url, isReload)
            }
        }
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