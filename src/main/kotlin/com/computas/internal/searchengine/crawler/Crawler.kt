package com.computas.internal.searchengine.crawler

import org.apache.log4j.Logger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.nio.file.Paths

/**
 * @author Lars Martin S. Pedersen on 20/02/2017.
 */
class Crawler(var saveTo: String = ".", baseUrl: String = "http://localhost/") {

    var baseUrl: String
    val visited = hashSetOf<String>()

    companion object {
        val log: Logger = Logger.getLogger(Crawler::class.java.name)
    }

    init {
        this.baseUrl = if (baseUrl.endsWith('/')) baseUrl else "$baseUrl/"
    }

    fun crawl(url: String) {
        val normalizedUrl = normalize(url)
        log.debug("Crawling $url...")
        if (normalizedUrl !in visited) {
            visited.add(url)
            val fetchedDocument = Jsoup.connect(normalizedUrl).get()
            writeHtml(fetchedDocument, url)
            val toCrawl = getUrls(document = fetchedDocument, baseUrl = url) - visited
            log.debug("Found ${toCrawl.size} new urls to crawl on $url...")
            for (foundUrl in toCrawl) {
                crawl(foundUrl)
            }
        }
        else {
            log.debug("$url already crawled.")
        }
    }

    private fun writeHtml(fetchedDocument: Document, url: String) {
        val filename = getOutputFilename(url)
        log.debug("Writing data from $url to $filename...")
        File(filename).printWriter().use { p ->
            p.write("<!-- URL: $url -->\n")
            p.write(fetchedDocument.outerHtml())
        }
    }

    fun getOutputFilename(url: String): String {
        val filename = url
                .replace("http://", "")
                .replace("https://", "")
                .replace("[^a-zA-Z0-9]".toRegex(), "_")
        return Paths.get(saveTo, "$filename.html").toString()

    }

    fun getUrls(document: Document, query: String = "a[href]", baseUrl: String = this.baseUrl): Set<String> {
        val elements = document.select(query)
        return elements
                .map { element -> element.absUrl("href").split("#")[0] }
                .filter { url -> url.startsWith(baseUrl) }
                .toHashSet()
    }

    private fun normalize(url: String): String {
        return url
    }


}
