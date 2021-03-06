package com.example.webimagedownloader.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class DownloadManagerUtil {

    val progress = MutableLiveData(0)

    private val tag = DownloadManagerUtil::class.java.simpleName

    fun download(scrapedImgUrls: List<String>, downloadPath: String, context: Context) {
        // val mydir = File(Environment.getExternalStorageDirectory().toString() + Environment.DIRECTORY_PICTURES)
        /*val mydir = File(downloadPath)
        if (!mydir.exists()) {
            mydir.mkdirs()
        }*/

        val downloadPath = downloadPath.substringAfterLast("/0")

        val mydir = File(Environment.getExternalStorageDirectory().toString() + downloadPath)
        if (!mydir.exists()) {
            mydir.mkdirs()
        }

        scrapedImgUrls.forEach { url ->
            Log.d(tag, "downloading: $url")
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadUri: Uri = Uri.parse(url)
            val request: DownloadManager.Request = DownloadManager.Request(downloadUri)

            val dateFormat = SimpleDateFormat("mmddyyyyhhmmss")
            val date: String = dateFormat.format(Date())

            val fileName = "$date.${extractFileExtension(url)}"

            request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
            )
                .setAllowedOverRoaming(false)
                .setTitle("Downloading")
                .setDestinationInExternalPublicDir(downloadPath, fileName)

            manager.enqueue(request)

            progress.postValue(progress.value?.plus(1))
        }
    }

    private fun extractFileExtension(fileName: String): String {
        return fileName.substring(fileName.lastIndexOf('.') + 1)
    }

}