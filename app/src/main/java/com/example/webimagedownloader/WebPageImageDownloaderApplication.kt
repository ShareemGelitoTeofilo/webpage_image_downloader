package com.example.webimagedownloader

import android.app.Application
import android.os.Environment
import com.example.webimagedownloader.utils.Constants
import com.example.webimagedownloader.utils.SharedPreferenceHelper
import com.example.webimagedownloader.utils.network.CheckNetwork
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class WebPageImageDownloaderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPreferenceHelper.create(this)
        // register listener for connectivity and notify app
        val network = CheckNetwork(this)
        network.registerNetworkCallback()

        initDefaultDownloadLocation()
    }

    private fun initDefaultDownloadLocation() {
        if (SharedPreferenceHelper.getString(Constants.URL, "") != "") {
            val downloadPath =
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Environment.getExternalStorageDirectory().toString()
                } else {
                    Environment.getExternalStorageDirectory()
                        .toString() + "/" + Environment.DIRECTORY_DCIM
                }
            SharedPreferenceHelper.addString(Constants.URL, downloadPath)
        }
    }
}