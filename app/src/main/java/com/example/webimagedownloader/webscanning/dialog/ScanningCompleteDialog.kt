package com.example.webimagedownloader.webscanning.dialog

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.webimagedownloader.R
import com.example.webimagedownloader.utils.DownloadManagerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ScanningCompleteDialog(val scrapedImgUrls: List<String>) : DialogFragment(),
    ActivityCompat.OnRequestPermissionsResultCallback {

    private val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: Int = 91231
    private var rootView: View? = null
    private var permissionGranted = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.dialog_fragment_scanning_complete, container)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView!!.findViewById<Button>(R.id.btnDownload).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (permissionGranted) {
                    DownloadManagerUtil.download(scrapedImgUrls, "", requireActivity())
                    dismiss()
                } else {
                    askPermissions()
                }
            }
        }
    }

    private fun askPermissions() {
        val checkSelfPermission = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
            permissionGranted = false
            //request for the permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
            )

            // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            permissionGranted = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    permissionGranted = true
                    DownloadManagerUtil.download(scrapedImgUrls, "", requireActivity())
                    // permission was granted, yay! Do the
                    // write to external strage operations here
                } else {

                    permissionGranted = false
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    /*You can forcefully again ask for the permissions to the user here but it is a bad practice*/
                }
                return
            }
        }
    }
}