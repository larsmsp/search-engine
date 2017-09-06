package com.computas.internal.searchengine

import com.computas.internal.searchengine.crawler.Crawler
import com.computas.internal.searchengine.index.Indexer
import com.computas.internal.searchengine.parser.Parser

/**
 * @author Lars Martin S. Pedersen on 16/03/2017.
 */

fun main(args: Array<String>) {
    if (args.size < 3) {
        usage()
        return
    }
    val method = args[0]

    if ("crawl".equals(method)) {
        Crawler(args[2], args[1]).crawl(args[1])
    }
    else if ("parse".equals(method)) {
        Parser(args[1], args[2]).parse()
    }
    else if ("index".equals(method)) {
        if (args.size < 3) {
            usageIndex()
        }
        else {
            Indexer(args[1], args[2]).index()
        }
    }
}

fun usage() {
    println("Usage: java -jar search-engine.jar <crawl/parse> <baseUrl/inputPath> <outputPath>")
}

fun usageIndex() {
    println("Usage: java -jar search-engine.jar index <inputPath> <api-url> <api-key>")
}
