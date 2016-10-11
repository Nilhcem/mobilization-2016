package com.nilhcem.mobilization.scraper.model.output

data class Session(val id: Int, val title: String, val description: String?, val speakersId: List<Int>?, val startAt: String, val duration: Int, val roomId: Int)
