package com.example.webimagedownloader.webscanning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.webimagedownloader.utils.Constants
import com.example.webimagedownloader.utils.SharedPreferenceHelper
import com.example.webimagedownloader.utils.htmlscraper.HtmlScraper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// class WebScanningViewModel(private val repository: WebScanningRepository): ViewModel() {
class WebScanningViewModel() : ViewModel() {

    companion object {
        val STATUS_STARTED = "started"
        val STATUS_ENDED = "ended"
        // val FACTORY = singleArgViewModelFactory(::WebScanningViewModel)
    }

    private val _scanningStatus = MutableLiveData<String>()

    val scanningStatus: LiveData<String>
        get() = _scanningStatus

    private val _scrapedImages = MutableLiveData<List<String>>()

    val scrapedImages: LiveData<List<String>>
        get() = _scrapedImages

    fun onScanBtnClicked(webUrl: String) {
        scan(webUrl)
    }

    private fun scan(webUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModelScope.launch {
                _scanningStatus.value = STATUS_STARTED
            }
            val result = HtmlScraper.scrape(webUrl, "img")
            viewModelScope.launch {
                _scrapedImages.value = result
                _scanningStatus.value = STATUS_ENDED
            }
        }
    }

    fun saveSearchUrlToPreference(searchUrl: String) {
        SharedPreferenceHelper.addString(Constants.SEARCH_URL, searchUrl)
    }
}