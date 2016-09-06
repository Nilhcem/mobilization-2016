package com.nilhcem.mobilization.ui.schedule.day;

import com.nilhcem.mobilization.data.app.model.ScheduleSlot;

import java.util.List;

public interface ScheduleDayMvp {

    interface View {
        void initSlotsList(List<ScheduleSlot> slots);

        void refreshSlotsList();
    }
}
