package com.nilhcem.mobilization.scraper.model

import com.nilhcem.mobilization.scraper.model.input.Break
import com.nilhcem.mobilization.scraper.model.input.ScheduleSlot
import com.nilhcem.mobilization.scraper.model.input.Talk
import com.nilhcem.mobilization.scraper.model.input.TimeSlot
import com.nilhcem.mobilization.scraper.model.output.Room
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.RegexOption.IGNORE_CASE
import com.nilhcem.mobilization.scraper.model.input.Speaker as ApiSpeaker
import com.nilhcem.mobilization.scraper.model.output.Session as AppSession
import com.nilhcem.mobilization.scraper.model.output.Speaker as AppSpeaker

object Mapper {

    private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

    fun convertSpeaker(id: Int, speaker: ApiSpeaker): AppSpeaker {
        val name = "${speaker.firstname} ${speaker.lastname}"
        val photo = "http://2016.mobilization.pl${speaker.photo_url}"
        val bio = speaker.bio_html.parseHtml()
        val website = speaker.www?.nullIfEmpty()
        val twitter = speaker.twitter?.nullIfEmpty()
        return AppSpeaker(id + 1, name, null, photo, bio, website, twitter, null)
    }

    fun convertSessions(timeSlots: Map<String, TimeSlot>, talks: Map<String, Talk>, breaks: Map<String, Break>, scheduleSlots: Map<String, ScheduleSlot>, speakers: Map<String, AppSpeaker>): List<AppSession> {
        val sessions = mutableListOf<AppSession>()

        timeSlots.forEach { timeSlot ->
            val scheduleSlot = scheduleSlots[timeSlot.key]!!

            if (scheduleSlot.breakKey != null) {
                val `break` = breaks[scheduleSlot.breakKey]!!
                sessions.add(createSession(sessions.size, `break`.title, `break`.description_html, null, Room.NONE, timeSlot.value))
            } else {
                scheduleSlot.talksKey!!.forEach { scheduleSlot ->
                    val room = Room.getByApiId(scheduleSlot.key)
                    val talk = talks[scheduleSlot.value]!!
                    val title = if ("EN" == talk.language.toUpperCase(Locale.US)) talk.title else "${talk.title} [${talk.language}]"
                    val description = talk.description_html
                    val speakersIds = talk.speakers_keys.map { speakers[it]!!.id }
                    sessions.add(createSession(sessions.size, title, description, speakersIds, room, timeSlot.value))
                }
            }
        }
        return sessions.toList()
    }

    private fun createSession(id: Int, title: String, summary: String, speakersIds: List<Int>?, room: Room, timeSlot: TimeSlot): AppSession {
        val description = summary.nullIfEmpty()?.parseHtml()

        val startAt = "2016-10-22 ${timeSlot.from.formatTime()}"
        val dateFrom = DATE_FORMAT.parse(startAt)
        val dateTo = DATE_FORMAT.parse("2016-10-22 ${timeSlot.to.formatTime()}")
        val duration = ((dateTo.time - dateFrom.time) / 60000).toInt()
        val roomId = room.id

        return AppSession(id + 1, title, description, speakersIds, startAt, duration, roomId)
    }

    private fun String.parseHtml() = Jsoup.clean(this, "", Whitelist.basic(),
            Document.OutputSettings().prettyPrint(false))
            .replace(Regex("&nbsp;", IGNORE_CASE), " ")
            .replace(Regex("&amp;", IGNORE_CASE), "&")
            .replace(Regex("&gt;", IGNORE_CASE), ">").replace(Regex("&lt;", IGNORE_CASE), "<")
            .replace(Regex("<br[\\s/]*>", IGNORE_CASE), "\n")
            .replace(Regex("<p>", IGNORE_CASE), "").replace(Regex("</p>", IGNORE_CASE), "\n")
            .replace(Regex("</?ul>", IGNORE_CASE), "")
            .replace(Regex("<li>", IGNORE_CASE), "• ").replace(Regex("</li>", IGNORE_CASE), "\n")
            .replace(Regex("\n\n• ", IGNORE_CASE), "\n• ")
            .replace(Regex("<a\\s[^>]*>", IGNORE_CASE), "").replace(Regex("</a>", IGNORE_CASE), "")
            .replace(Regex("</?strong>", IGNORE_CASE), "")
            .replace(Regex("</?em>", IGNORE_CASE), "")
            .replace(Regex("\\s*\n\\s*"), "\n").replace(Regex("^\n"), "").replace(Regex("\n$"), "")

    private fun String.nullIfEmpty() = with(trim()) { if (isEmpty()) null else this }

    private fun String.formatTime() = if (length == 4) "0$this" else this
}
