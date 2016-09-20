package com.nilhcem.mobilization.scraper.api

import com.nilhcem.mobilization.scraper.model.input.*
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface MobilizationApi {

    companion object {
        private val ENDPOINT = "http://2016.mobilization.pl/api-demo/"

        val MOSHI = Moshi.Builder()
                .add(ScheduleSlotAdapter())
                .build()

        val SERVICE = Retrofit.Builder()
                .client(OkHttpClient())
                .baseUrl(ENDPOINT)
                .addConverterFactory(MoshiConverterFactory.create(MOSHI))
                .build()
                .create(MobilizationApi::class.java)
    }

    @GET("slots.json")
    fun getSlots(): Call<Map<String, TimeSlot>>

    @GET("speakers.json")
    fun getSpeakers(): Call<Map<String, Speaker>>

    @GET("talks.json")
    fun getTalks(): Call<Map<String, Talk>>

    @GET("breaks.json")
    fun getBreaks(): Call<Map<String, Break>>

    @GET("schedule.json")
    fun getSchedule(): Call<Map<String, ScheduleSlot>>
}
