package com.nilhcem.mobilization.scraper.model.output

// http://2016.mobilization.pl/api/venues.json
enum class Room(val id: Int, val apiId: String, val roomName: String) {

    NONE(0, "", ""),
    ROOM_1(1, "venue-rndity", "Rndity"),
    ROOM_2(2, "venue-ericpol", "Ericpol"),
    ROOM_3(3, "venue-mobica", "Mobica"),
    ROOM_4(4, "venue-seqr", "SEQR"),
    ROOM_5(5, "venue-tomtom", "TomTom");

    companion object {
        fun getByApiId(apiId: String) = Room.values().filter { apiId == it.apiId }.getOrElse(0, { NONE })
    }
}
