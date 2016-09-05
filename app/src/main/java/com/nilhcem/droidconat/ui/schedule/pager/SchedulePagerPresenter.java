package com.nilhcem.droidconat.ui.schedule.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.nilhcem.droidconat.data.app.DataProvider;
import com.nilhcem.droidconat.data.app.model.Schedule;
import com.nilhcem.droidconat.ui.BaseFragmentPresenter;

import icepick.State;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SchedulePagerPresenter extends BaseFragmentPresenter<SchedulePagerMvp.View> implements SchedulePagerMvp.Presenter {

    @State Schedule schedule;

    private final DataProvider dataProvider;
    private Subscription scheduleSubscription;

    public SchedulePagerPresenter(SchedulePagerMvp.View view, DataProvider dataProvider) {
        super(view);
        this.dataProvider = dataProvider;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (schedule == null) {
            loadData();
        } else {
            this.view.displaySchedule(schedule);
        }
    }

    @Override
    public void reloadData() {
        loadData();
    }

    @Override
    public void onStop() {
        if (scheduleSubscription != null) {
            scheduleSubscription.unsubscribe();
        }
        super.onStop();
    }

    private void loadData() {
        scheduleSubscription = dataProvider.getSchedule()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scheduleDays -> schedule = scheduleDays,
                        throwable -> Timber.e(throwable, "Error getting schedule"),
                        () -> {
                            if (schedule == null) {
                                view.displayLoadingError();
                            } else {
                                view.displaySchedule(schedule);
                            }
                        });
    }
}
