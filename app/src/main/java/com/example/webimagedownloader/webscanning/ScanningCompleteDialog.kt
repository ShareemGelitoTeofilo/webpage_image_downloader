package com.example.webimagedownloader.webscanning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.webimagedownloader.R
import com.example.webimagedownloader.utils.DownloadManagerUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ScanningCompleteDialog(val scrapedImgUrls: List<String>) : DialogFragment() {

    private var rootView: View? = null

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
                DownloadManagerUtil.download(scrapedImgUrls, "", requireActivity())
            }
        }
    }
}