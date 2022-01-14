package com.example.webimagedownloader.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.webimagedownloader.R
import com.example.webimagedownloader.htmlscraper.HtmlScraper
import com.example.webimagedownloader.network.CheckNetwork
import com.example.webimagedownloader.network.NetworkVariable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
            CoroutineScope(Dispatchers.IO).launch {

                HtmlScraper.scrape("")
            }
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