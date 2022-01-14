package com.example.webimagedownloader.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.webimagedownloader.R
import com.example.webimagedownloader.network.CheckNetwork
import com.example.webimagedownloader.network.NetworkVariable
import com.example.webimagedownloader.utils.Constants
import com.example.webimagedownloader.webscanning.WebsiteScanningActivity
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // register listener for connectivity and notify app
        val network = CheckNetwork(applicationContext)
        network.registerNetworkCallback()

        findViewById<View>(R.id.btnSearch).setOnClickListener {
            val url = findViewById<TextInputEditText>(R.id.editTextUrl).text.toString()
            openWebsiteScannerScreen(url)

            /* TODO checkConnectivityAndExecute {
                CoroutineScope(Dispatchers.IO).launch {
                    // Use WorkerManager
                    val url = findViewById<TextInputEditText>(R.id.editTextUrl).text.toString()
                    HtmlScraper.scrape(url)
                }
            }*/
        }
    }

    private fun openWebsiteScannerScreen(url: String) {
        val intent = Intent(this, WebsiteScanningActivity::class.java).apply {
            putExtra(Constants.URL, url)
        }
        startActivity(intent)
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