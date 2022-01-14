package com.example.webimagedownloader.webscanning

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.webimagedownloader.R
import com.example.webimagedownloader.utils.Constants

class WebsiteScanningActivity : AppCompatActivity() {


    private lateinit var webView: WebView
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_website_scanning)

        intent?.extras?.let {
            url = it.getString(Constants.URL)
        }

        url?.let {
          displayWebsite(it)
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
}