package com.nilhcem.droidconat.scraper.model.input

data class Speaker(val id: String, val name: String, val surname: String, val company: String, val title: String?, val bio: String, val thumbnailUrl: String, val rockstar: Boolean?, val social: List<SocialLink>?)

data class SocialLink(val name: String, val link: String)
