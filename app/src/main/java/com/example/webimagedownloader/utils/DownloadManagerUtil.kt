package com.example.webimagedownloader.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class DownloadManagerUtil {


    companion object {

        private val tag = DownloadManagerUtil::class.java.simpleName

        fun download(scrapedImgUrls: List<String>, downloadPath: String, context: Context) {
            val mydir = File(Environment.getExternalStorageDirectory().toString() + Environment.DIRECTORY_PICTURES)
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

                request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
                )
                    .setAllowedOverRoaming(false)
                    .setTitle("Downloading")
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "$date.jpg")

                manager.enqueue(request)
                // TODO return mydir.getAbsolutePath() + File.separator.toString() + date + ".jpg"
            }
        }
    }
}