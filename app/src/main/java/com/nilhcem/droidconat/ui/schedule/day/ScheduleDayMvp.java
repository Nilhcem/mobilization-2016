package com.nilhcem.droidconat.ui.schedule.day;

import com.nilhcem.droidconat.data.app.model.ScheduleSlot;

import java.util.List;

public interface ScheduleDayMvp {

    interface View {
        void initSlotsList(List<ScheduleSlot> slots);

        void refreshSlotsList();
    }
}
