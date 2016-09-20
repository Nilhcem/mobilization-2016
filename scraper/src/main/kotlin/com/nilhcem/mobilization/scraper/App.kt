package com.nilhcem.mobilization.scraper

import com.nilhcem.mobilization.scraper.api.MobilizationApi
import com.nilhcem.mobilization.scraper.model.output.Session
import com.nilhcem.mobilization.scraper.model.output.Speaker
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Types
import okio.Buffer
import java.io.File

fun main(args: Array<String>) {
    with(Scraper(MobilizationApi.SERVICE)) {
        createJsons(speakers, sessions)
    }
}

fun createJsons(speakers: List<Speaker>, sessions: List<Session>) {
    val moshi = MobilizationApi.MOSHI
    File("output").mkdir()

    File("output/speakers.json").printWriter().use { out ->
        val buffer = Buffer()
        val jsonWriter = JsonWriter.of(buffer)
        jsonWriter.use {
            jsonWriter.setIndent("  ")
            val adapter: JsonAdapter<List<Speaker>> = moshi.adapter(Types.newParameterizedType(List::class.java, Speaker::class.java))
            adapter.toJson(jsonWriter, speakers)
            out.println(buffer.readUtf8())
        }
    }

    File("output/sessions.json").printWriter().use { out ->
        val buffer = Buffer()
        val jsonWriter = JsonWriter.of(buffer)
        jsonWriter.use {
            jsonWriter.setIndent("  ")
            val adapter: JsonAdapter<List<Session>> = moshi.adapter(Types.newParameterizedType(List::class.java, Session::class.java))
            adapter.toJson(jsonWriter, sessions)
            out.println(buffer.readUtf8())
        }
    }
}
