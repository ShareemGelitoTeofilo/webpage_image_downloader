package com.example.webimagedownloader.webscanning

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import com.example.webimagedownloader.R
import com.example.webimagedownloader.utils.htmlscraper.HtmlScraper
import com.example.webimagedownloader.utils.network.CheckNetwork
import com.example.webimagedownloader.utils.network.NetworkVariable
import com.example.webimagedownloader.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WebsiteScanningActivity : AppCompatActivity() {


    private lateinit var webView: WebView
    private var url: String? = null
    private lateinit var scanningProgressDialog: ScanningProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_website_scanning)

        scanningProgressDialog = ScanningProgressDialog()

        // register listener for connectivity and notify app
        val network = CheckNetwork(applicationContext)
        network.registerNetworkCallback()

        intent?.extras?.let {
            url = it.getString(Constants.URL)
        }

        url?.let { url ->
            displayWebsite(url)
            findViewById<Button>(R.id.btnWebsiteScan).setOnClickListener {
                checkConnectivityAndExecute {
                    scanningProgressDialog.show(supportFragmentManager, "scanning_progress_dialog")
                    CoroutineScope(Dispatchers.IO).launch {
                        // TODO Use WorkerManager
                        val scrapedImgUrls = HtmlScraper.scrape(url, "img")
                        onScanningCompleted(scrapedImgUrls)
                    }
                }
            }
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

    // if you press Back button this code will work
    override fun onBackPressed() {
        // if your webview can go back it will go back
        if (webView.canGoBack())
            webView.goBack()
        // if your webview cannot go back
        // it will exit the application
        else
            super.onBackPressed()
    }

    private fun checkConnectivityAndExecute(task: () -> Unit) {
        if (NetworkVariable.isNetworkConnected) {
            // Internet Connected
            task()
            Log.d("Network", "Internet connected")
        } else {
            // Not Connected
            Log.d("Network", "Internet disconnected")
        }
    }

    private fun onScanningCompleted(scrapedImgUrls: List<String>) {
        if (scanningProgressDialog.isVisible) {
            scanningProgressDialog.dismiss()
        }

        val scanningCompleteDialog = ScanningCompleteDialog(scrapedImgUrls)
        scanningCompleteDialog.show(supportFragmentManager, "scanning_completed_dialog")
    }
}