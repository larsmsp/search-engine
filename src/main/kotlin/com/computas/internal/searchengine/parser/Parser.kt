package com.computas.internal.searchengine.parser

import com.beust.klaxon.json
import org.jsoup.Jsoup
import org.apache.log4j.Logger
import java.io.BufferedReader
import java.io.File
import java.nio.file.Paths

/**
 * @author Lars Martin S. Pedersen on 16/03/2017.
 */
class Parser(var readFrom: String = ".", var saveTo: String = ".") {

    val URL_PATTERN = "^<!-- URL: (.*) -->$"

    companion object {
        val log: Logger = Logger.getLogger(Parser::class.java.name)
    }

    fun parse() {
        for (file in File(readFrom).walk().maxDepth(1)
                .filter { f -> f.isFile }
                .filter { f -> f.name.endsWith(".html") || f.name.endsWith(".htm") }) {
            var json = ""
            file.bufferedReader().use { reader ->
                var url: String? = getPageUrl(reader)
                log.debug("Found url: $url")
                val document = Jsoup.parse(reader.readText())
                val title = document.title()
                val body = document.body().select("main > div.container")
                if (body.isNotEmpty()) {
                    json = getJsonDocument(url, title, body[0].text())
                }
            }
            val outputFile = Paths.get(saveTo, "${file.nameWithoutExtension}.json")
            File(outputFile.toString()).printWriter().use { writer ->
                writer.write(json)
            }
        }

    }

    private fun getPageUrl(reader: BufferedReader): String? {
        var url: String? = ""
        for (line in reader.lines()) {
            val urlRegex = URL_PATTERN.toRegex()
            url = urlRegex.matchEntire(line)?.groups?.get(1)?.value!!
            if (!url.equals("")) {
                break
            }
        }
        return url
    }

    fun getJsonDocument(url: String?, title: String, contents: String): String {
        val document = json {
            obj(
                    "id" to url,
                    "title" to title,
                    "contents" to contents,
                    "url" to url
            )
        }
        return document.toJsonString()
    }
}