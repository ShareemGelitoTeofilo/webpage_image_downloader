package com.example.webimagedownloader.main

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.webimagedownloader.utils.Constants
import com.example.webimagedownloader.utils.SharedPreferenceHelper
import com.example.webimagedownloader.utils.UriUtils
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _downloadPath = MutableLiveData<String>()

    val downloadPath: LiveData<String>
        get() = _downloadPath


    fun initDownloadUrl() {
        val url = SharedPreferenceHelper.getString(Constants.URL, "")
        url?.let { value ->
            if (value != "") {
                _downloadPath.value = value
            }
        }
    }

    fun processDownloadDirectory(uri: Uri, context: Context) {
        viewModelScope.launch {
            val fullFilePath = getFullPath(uri, context)
            savePathToPreferences(fullFilePath)
            _downloadPath.value = fullFilePath
        }
    }

    private fun savePathToPreferences(downloadPath: String) {
        SharedPreferenceHelper.addString(Constants.URL, downloadPath)
    }

    private fun getFullPath(uri: Uri, context: Context): String {
        val docUriTree = DocumentsContract.buildDocumentUriUsingTree(
            uri,
            DocumentsContract.getTreeDocumentId(uri)
        )
        return UriUtils.getPathFromUri(context, docUriTree)
    }
}