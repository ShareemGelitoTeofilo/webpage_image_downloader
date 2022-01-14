package com.example.webimagedownloader.utils.htmlscraper

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class HtmlScraper {

    companion object {
        private val tag: String = HtmlScraper::class.java.simpleName

        // TODO scrape all image file types to download
        fun scrape(url: String) {
            /*val doc: Document = Jsoup.connect("https://en.wikipedia.org/").get()
            Log.d(tag, "Doc title: ${doc.title()}")
            val newsHeadlines: Elements = doc.select("#mp-itn b a")
            for (headline in newsHeadlines) {
                val logText = "${headline.attr("title")} ${headline.absUrl("href")}"
                Log.d(tag, logText)
            }*/

            val doc: Document =
                Jsoup.connect(url)
                    .get()
            val media = doc.select("[src]")
            Log.d(tag, "\nMedia: (${media.size})")
            for (src in media) {
                if (src.normalName().equals("img")) {
                    Log.d(tag,
                        " * ${src.tagName()}: <${src.attr("abs:src")}> ${src.attr("width")}x${src.attr("height")}")

                    /*Log.d(tag,
                        " * ${src.tagName()}: <${src.attr("abs:src")}> ${src.attr("width")}x${src.attr("height")} (${src.attr("alt").substring(0..20)})")*/
                } else {
                    Log.d(tag," * ${src.tagName()}: <${src.attr("abs:src")}>")
                }
            }

        }
    }
}