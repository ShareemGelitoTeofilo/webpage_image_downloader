package com.example.webimagedownloader

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Debug
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.webimagedownloader.htmlscraper.HtmlScraper
import com.example.webimagedownloader.network.CheckNetwork
import com.example.webimagedownloader.network.NetworkVariable


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val network = CheckNetwork(applicationContext)
        network.registerNetworkCallback()

        // Check network connection

        // Check network connection
        findViewById<View>(R.id.txt_greeting).setOnClickListener {
            checkConnectivity()

            HtmlScraper.scrape("")
        }
    }

    private fun checkConnectivity() {
        if (NetworkVariable.isNetworkConnected) {
            // Internet Connected
            Log.d("Network", "Internet connected")
        } else {
            // Not Connected
            Log.d("Network", "Internet disconnected")
        }
    }
}