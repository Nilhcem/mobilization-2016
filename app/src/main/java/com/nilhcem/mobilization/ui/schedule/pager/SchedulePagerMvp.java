package com.nilhcem.mobilization.ui.schedule.pager;

import com.nilhcem.mobilization.data.app.model.Schedule;

public interface SchedulePagerMvp {

    interface View {
        void displaySchedule(Schedule schedule);

        void displayLoadingError();
    }

    interface Presenter {
        void reloadData();
    }
}
