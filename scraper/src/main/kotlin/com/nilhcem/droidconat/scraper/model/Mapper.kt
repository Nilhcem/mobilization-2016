package com.nilhcem.droidconat.scraper.model

import com.nilhcem.droidconat.scraper.model.input.ScheduleDay
import com.nilhcem.droidconat.scraper.model.input.SocialLink
import com.nilhcem.droidconat.scraper.model.output.Room
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.RegexOption.IGNORE_CASE
import com.nilhcem.droidconat.scraper.model.input.Session as ApiSession
import com.nilhcem.droidconat.scraper.model.input.Speaker as ApiSpeaker
import com.nilhcem.droidconat.scraper.model.output.Session as AppSession
import com.nilhcem.droidconat.scraper.model.output.Speaker as AppSpeaker

object Mapper {

    private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

    fun convertSpeaker(id: Int, speaker: ApiSpeaker): AppSpeaker {
        val name = "${speaker.name} ${speaker.surname}"
        val title = listOfNotNull(speaker.title, speaker.company).joinToString(", ")
        val photo = "https://droidcon.at/img/people/${speaker.thumbnailUrl}"
        val bio = speaker.bio.parseHtml()
        val twitterUrl = speaker.social?.filter { it.name == "twitter" }?.getLink()
        val twitterHandler = getHandleFromUrl(twitterUrl)
        val githubUrl = speaker.social?.filter { it.name == "github" }?.getLink()
        val githubHandle = getHandleFromUrl(githubUrl)
        val website = speaker.social?.filter { it.name != "twitter" && it.name != "github" }?.getLink()

        return AppSpeaker(id + 1, name, title, photo, bio, website, twitterHandler, githubHandle)
    }

    fun convertSession(speakersMap: Map<String, AppSpeaker>, sessionsMap: Map<Int, ApiSession>, days: List<ScheduleDay>): List<AppSession> {
        val sessions = mutableListOf<AppSession>()

        days.forEach { day ->
            val roomIds = day.tracks.map { Room.getRoomId(it.title) }

            day.timeslots.forEach { time ->
                val startAt = "${day.date} ${time.startTime}"
                val dateFrom = DATE_FORMAT.parse(startAt)
                val dateTo = DATE_FORMAT.parse("${day.date} ${time.endTime}")
                val duration = ((dateTo.time - dateFrom.time) / 60000).toInt()

                time.sessionIds.forEachIndexed { i, sessionId ->
                    val roomId = roomIds[i]
                    val session = sessionsMap[sessionId]

                    if (session != null) {
                        sessions.add(createSession(sessions.size, speakersMap, startAt, duration, roomId, session))
                    }
                }
            }
        }

        return sessions.toList()
    }

    private fun createSession(id: Int, speakersMap: Map<String, AppSpeaker>, startAt: String, duration: Int, roomId: Int, session: ApiSession): AppSession {
        val title = session.title.parseHtml()
        val description = session.description?.parseHtml()
        val speakersIds = session.speakers?.map { speakersMap[it]?.id }?.filterNotNull()
        val room = if (session.service ?: false) Room.NONE.id else roomId
        return AppSession(id + 1, title, description, speakersIds, startAt, duration, room)
    }

    private fun getHandleFromUrl(url: String?): String? {
        if (url == null) {
            return null
        }

        val urlWithoutLastSlash = if (url.last() == '/') url.substring(0, url.length - 1) else url
        return urlWithoutLastSlash.substring(urlWithoutLastSlash.lastIndexOf("/") + 1)
    }

    private fun List<SocialLink>.getLink() = this.map { it.link }.firstOrNull()

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
}
