package com.example.webimagedownloader.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.webimagedownloader.R
import com.example.webimagedownloader.utils.network.NetworkVariable

fun checkConnectivityAndExecute(context: Context, task: () -> Unit) {
    if (NetworkVariable.isNetworkConnected) {
        // Internet Connected
        task()
        Log.d("Network", "Internet connected")
    } else {
        // Not Connected
        Toast.makeText(context, context.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show()
        Log.d("Network", "Internet disconnected")
    }
}