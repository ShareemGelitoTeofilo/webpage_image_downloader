package com.example.webimagedownloader.webscanning

import android.annotation.SuppressLint
import android.os.Bundle
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WebsiteScanningActivity : AppCompatActivity() {


    private lateinit var webView: WebView
    private var url: String? = null
    private lateinit var scanningProgressDialog: ScanningProgressDialog
    private val scrapedImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_website_scanning)
        scanningProgressDialog = ScanningProgressDialog()
        intent?.extras?.let {
            url = it.getString(Constants.URL)
        }

        val viewModel = ViewModelProvider(this)[WebScanningViewModel::class.java]
        initObservers(viewModel)

        url?.let { url ->
            displayWebsite(url)
            findViewById<Button>(R.id.btnWebsiteScan).setOnClickListener {
                checkConnectivityAndExecute(applicationContext) {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.onScanBtnClicked(url)
                    }
                }
            }
        }
    }

    private fun initObservers(viewModel: WebScanningViewModel) {
        viewModel.scanningStatus.observe(this) {
            when (it) {
                WebScanningViewModel.STATUS_STARTED -> {
                    scanningProgressDialog.show(supportFragmentManager, "scanning_progress_dialog")
                }
                WebScanningViewModel.STATUS_ENDED -> {
                    if (scanningProgressDialog.isVisible) {
                        scanningProgressDialog.dismiss()
                    }
                    onScanningCompleted(scrapedImages)
                }
            }
        }

        viewModel.scrapedImages.observe(this) {
            scrapedImages.clear()
            scrapedImages.addAll(it)
        }
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

    private fun onScanningCompleted(scrapedImgUrls: List<String>) {
        val scanningCompleteDialog = ScanningCompleteDialog(scrapedImgUrls)
        scanningCompleteDialog.show(supportFragmentManager, "scanning_completed_dialog")
    }
}