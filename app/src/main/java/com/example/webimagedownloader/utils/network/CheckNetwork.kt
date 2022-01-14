package com.example.webimagedownloader.utils.network

import android.content.Context
import android.net.Network

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback

import android.net.NetworkRequest
import android.util.Log
import android.widget.Toast
import com.example.webimagedownloader.R
import java.lang.Exception

/*
You need to call the below method once. It register the callback and fire it when there is a change in network state.
Here I used a Global Static Variable, So I can use it to access the network state in anywhere of the application.
You need to call the below method once. It register the callback and fire it when there is a change in network state.
Here I used a Global Static Variable, So I can use it to access the network state in anywhere the application.
*/
// You need to pass the context when creating the class
class CheckNetwork(private val context: Context) {

    // Network Check
    fun registerNetworkCallback() {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val builder = NetworkRequest.Builder()
            connectivityManager.registerDefaultNetworkCallback(object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    NetworkVariable.isNetworkConnected = true // Global Static Variable
                    Log.d("Network", "Internet connected")
                }

                override fun onLost(network: Network) {
                    NetworkVariable.isNetworkConnected = false // Global Static Variable
                    Toast.makeText(context, context.resources.getString(R.string.internet_connection_error), Toast.LENGTH_SHORT).show()
                    Log.d("Network", "Internet disconnected")
                }
            })
            NetworkVariable.isNetworkConnected = false
        } catch (e: Exception) {
            NetworkVariable.isNetworkConnected = false
        }
    }

    companion object {
        fun isInternetConnected(context: Context): Boolean {
            val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}