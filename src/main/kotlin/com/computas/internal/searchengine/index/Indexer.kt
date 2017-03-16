package com.computas.internal.searchengine.index

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.io.File
import khttp.post
import org.apache.log4j.Logger

/**
 * @author Lars Martin S. Pedersen on 16/03/2017.
 */
class Indexer(var readFrom: String = ".", var apiUrl: String, var apiKey: String) {

    companion object {
        val log: Logger = Logger.getLogger(com.computas.internal.searchengine.index.Indexer::class.java.name)
    }

    val url: String
        get() = if (apiUrl.endsWith("/")) "$apiUrl$apiKey/put" else "$apiUrl/$apiKey/put"

    fun index() {
        val documents = mutableListOf<Map<String, Any?>>()
        for (file in File(readFrom).walk()
                .maxDepth(1)
                .filter { f -> f.name.endsWith(".json") }) {
            file.bufferedReader().use { reader ->
                val document = Parser().parse(reader) as JsonObject
                documents.add(document.map)
            }
        }
        val response = post(url, json = documents)
        if (response.statusCode != 200) {
            log.error("Request was not successful!")
        }
        else {
            log.info("Indexed ${response.jsonObject["indexed_documents"]} documents.")
        }
    }

}