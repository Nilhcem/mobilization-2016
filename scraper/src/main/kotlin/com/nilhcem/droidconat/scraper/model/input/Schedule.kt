package com.nilhcem.droidconat.scraper.model.input

data class ScheduleDay(val date: String, val dateReadable: String, val tracks: List<ScheduleTrack>, val timeslots: List<ScheduleTimeSlot>)

data class ScheduleTrack(val title: String, val color: String)

data class ScheduleTimeSlot(val startTime: String, val endTime: String, val sessionIds: List<Int>)
