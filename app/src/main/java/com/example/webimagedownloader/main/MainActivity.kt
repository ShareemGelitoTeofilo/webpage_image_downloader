package com.example.webimagedownloader.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.webimagedownloader.R
import com.example.webimagedownloader.htmlscraper.HtmlScraper
import com.example.webimagedownloader.network.CheckNetwork
import com.example.webimagedownloader.network.NetworkVariable
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // register listener for connectivity and notify app
        val network = CheckNetwork(applicationContext)
        network.registerNetworkCallback()

        findViewById<View>(R.id.btnSearch).setOnClickListener {
            checkConnectivityAndExecute {
                CoroutineScope(Dispatchers.IO).launch {
                    // Use WorkerManager
                    val url = findViewById<TextInputEditText>(R.id.editTextUrl).text.toString()
                    HtmlScraper.scrape(url)
                }
            }
        }
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
}