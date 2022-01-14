package com.example.webimagedownloader.htmlscraper

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


class HtmlScraper {

    companion object {
        private val tag: String = HtmlScraper::class.java.simpleName
        fun scrape(url: String) {
            val doc: Document = Jsoup.connect("https://en.wikipedia.org/").get()
            Log.d(tag, "Doc title: ${doc.title()}")
            val newsHeadlines: Elements = doc.select("#mp-itn b a")
            for (headline in newsHeadlines) {
                val logText = "${headline.attr("title")} ${headline.absUrl("href")}"
                Log.d(tag, logText)
            }
        }
    }
}