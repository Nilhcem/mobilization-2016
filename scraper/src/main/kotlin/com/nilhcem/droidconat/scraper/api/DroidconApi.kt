package com.nilhcem.droidconat.scraper.api

import com.nilhcem.droidconat.scraper.model.input.ScheduleDay
import com.nilhcem.droidconat.scraper.model.input.Session
import com.nilhcem.droidconat.scraper.model.input.Speaker
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface DroidconApi {

    companion object {
        private val ENDPOINT = "https://droidcon.at/json/en/"

        val MOSHI = Moshi.Builder().build()

        val SERVICE = Retrofit.Builder()
                .client(OkHttpClient())
                .baseUrl(ENDPOINT)
                .addConverterFactory(MoshiConverterFactory.create(MOSHI))
                .build()
                .create(DroidconApi::class.java)
    }

    @GET("schedule/schedule-data.json")
    fun getSchedule(): Call<List<ScheduleDay>>

    @GET("schedule/sessions-data.json")
    fun getSessions(): Call<List<Session>>

    @GET("speakers/speakers-data.json")
    fun getSpeakers(): Call<List<Speaker>>
}
