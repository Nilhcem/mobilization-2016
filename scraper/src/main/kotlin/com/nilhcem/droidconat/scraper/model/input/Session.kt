package com.nilhcem.droidconat.scraper.model.input

data class Session(val id: Int, val title: String, val place: String?, val service: Boolean?, val description: String?, val subtype: String?, val speakers: List<String>?, val complexity: String?)
