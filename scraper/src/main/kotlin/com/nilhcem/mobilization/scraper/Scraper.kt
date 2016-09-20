package com.nilhcem.mobilization.scraper

import com.nilhcem.mobilization.scraper.api.MobilizationApi
import com.nilhcem.mobilization.scraper.model.Mapper
import com.nilhcem.mobilization.scraper.model.output.Session
import com.nilhcem.mobilization.scraper.model.output.Speaker
import com.nilhcem.mobilization.scraper.model.input.Speaker as ApiSpeaker

class Scraper(api: MobilizationApi) {

    lateinit var speakers: List<Speaker>
    lateinit var sessions: List<Session>

    init {
        println("Start scraping")

        val speakersMap = api.getSpeakers().execute().body()
                .map { it.value.copy(apiId = it.key) }
                .toList()
                .mapIndexed { i, speaker -> speaker.apiId to Mapper.convertSpeaker(i, speaker) }
                .toMap()
                .mapKeys { it.key as String }

        val slots = api.getSlots().execute().body()
        val talks = api.getTalks().execute().body()
        val breaks = api.getBreaks().execute().body()
        val schedule = api.getSchedule().execute().body()

        speakers = speakersMap.values.toList()
        sessions = Mapper.convertSessions(slots, talks, breaks, schedule, speakersMap)
    }
}
