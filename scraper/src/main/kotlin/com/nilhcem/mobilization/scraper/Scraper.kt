package com.nilhcem.mobilization.scraper

import com.nilhcem.mobilization.scraper.api.DroidconApi
import com.nilhcem.mobilization.scraper.model.Mapper
import com.nilhcem.mobilization.scraper.model.output.Session
import com.nilhcem.mobilization.scraper.model.output.Speaker

class Scraper(val api: DroidconApi) {

    lateinit var speakers: List<Speaker>
    lateinit var sessions: List<Session>

    init {
        println("Start scraping")

        val speakersMap = api.getSpeakers().execute().body()
                .mapIndexed { i, speaker -> speaker.id to Mapper.convertSpeaker(i, speaker) }
                .toMap()

        val sessionsMap = api.getSessions().execute().body()
                .filter { it.id != 999 }
                .map { it.id to it }
                .toMap()

        val scheduleDays = api.getSchedule().execute().body()

        speakers = speakersMap.values.toList()
        sessions = Mapper.convertSession(speakersMap, sessionsMap, scheduleDays)
    }
}
