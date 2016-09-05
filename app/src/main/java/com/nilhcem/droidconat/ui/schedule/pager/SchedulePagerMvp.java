package com.nilhcem.droidconat.ui.schedule.pager;

import com.nilhcem.droidconat.data.app.model.Schedule;

public interface SchedulePagerMvp {

    interface View {
        void displaySchedule(Schedule schedule);

        void displayLoadingError();
    }

    interface Presenter {
        void reloadData();
    }
}
