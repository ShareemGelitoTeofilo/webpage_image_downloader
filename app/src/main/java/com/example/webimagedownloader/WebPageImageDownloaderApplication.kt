package com.example.webimagedownloader

import android.app.Application
import com.example.webimagedownloader.utils.SharedPreferenceHelper

class WebPageImageDownloaderApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPreferenceHelper.create(this)
    }
}