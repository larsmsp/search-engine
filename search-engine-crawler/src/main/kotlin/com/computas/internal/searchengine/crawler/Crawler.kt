package com.computas.internal.searchengine.crawler

import org.apache.log4j.Logger
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * @author Lars Martin S. Pedersen on 20/02/2017.
 */
class Crawler {

    var baseUrl: String
    var saveTo: String
    val visited = hashSetOf<String>()

    companion object {
        val log: Logger = Logger.getLogger(Crawler::class.java.name)
    }

    constructor(saveTo: String = ".", baseUrl: String = "http://localhost/") {
        this.saveTo = saveTo
        this.baseUrl = if (baseUrl.endsWith('/')) baseUrl else "$baseUrl/"
    }

    fun crawl(url: String) {
        val normalizedUrl = normalize(url)
        log.debug("Crawling $url...")
        if (normalizedUrl !in visited) {
            visited.add(url)
            val fetchedDocument = Jsoup.connect(normalizedUrl).get()
            val toCrawl = getUrls(document = fetchedDocument, baseUrl = url) - visited
            log.debug("Found ${toCrawl.size} new urls to crawl on $url...")
            for (foundUrl in toCrawl) {
                crawl(foundUrl)
            }
        }
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
