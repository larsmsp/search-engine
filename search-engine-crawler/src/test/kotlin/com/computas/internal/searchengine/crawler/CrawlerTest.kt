package com.computas.internal.searchengine.crawler

import org.jsoup.Jsoup
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Lars Martin S. Pedersen on 22/02/2017.
 */
class CrawlerTest {

    val crawler = Crawler()

    val html = """
    <html>
    <body>
    <a href="http://localhost/subpage/index.html">Absolute link</a>
    <a href="relativepage.html">Relative link</a>
    <a href="/relativepage.html">Relative link</a>
    <a href="relativepath/">Relative path</a>
    <a href="http://www.google.com">Link on another domain</a>
    </body>
    </html>
"""

    @Test
    fun getUrlsReturnsOnlyRelativeUrlsAndUrlsWithinRootUrl() {
        val document = Jsoup.parse(html, crawler.baseUrl)
        assertEquals(3, crawler.getUrls(document).size)
    }

    @Test
    fun crawl() {
        crawler.crawl("http://computas.com/")
        crawler.visited
    }
}