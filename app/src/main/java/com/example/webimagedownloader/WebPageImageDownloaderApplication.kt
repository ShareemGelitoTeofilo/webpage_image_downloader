package com.example.webimagedownloader

import android.app.Application
import com.example.webimagedownloader.utils.SharedPreferenceHelper
import com.example.webimagedownloader.utils.network.CheckNetwork

class WebPageImageDownloaderApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPreferenceHelper.create(this)
        // register listener for connectivity and notify app
        val network = CheckNetwork(this)
        network.registerNetworkCallback()
    }
}