package com.nilhcem.mobilization.scraper.model.input

import com.squareup.moshi.FromJson

data class ScheduleSlot(val breakKey: String?, val talksKey: Map<String, String>?)

class ScheduleSlotAdapter {
    @FromJson fun fromJson(data: Any): ScheduleSlot {
        if (data is Map<*, *>) {
            if (data.contains("break_key")) {
                return ScheduleSlot(data["break_key"] as String, null)
            } else {
                return ScheduleSlot(null, data.mapKeys { it.key as String }.mapValues { (it.value as Map<*, *>)["talk_key"] as String })
            }
        }
        throw UnsupportedOperationException("We should have never been there")
    }
}
