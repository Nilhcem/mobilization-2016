package com.nilhcem.droidconat.ui.speakers.list;

import com.nilhcem.droidconat.data.app.DataProvider;
import com.nilhcem.droidconat.data.app.model.Speaker;
import com.nilhcem.droidconat.ui.BaseFragmentPresenter;

import java.util.ArrayList;
import java.util.Collections;

import icepick.State;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SpeakersListPresenter extends BaseFragmentPresenter<SpeakersListMvp.View> implements SpeakersListMvp.Presenter {

    @State ArrayList<Speaker> speakers;

    private final DataProvider dataProvider;
    private Subscription speakersSubscription;

    public SpeakersListPresenter(SpeakersListMvp.View view, DataProvider dataProvider) {
        super(view);
        this.dataProvider = dataProvider;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (speakers == null) {
            loadData();
        } else {
            this.view.displaySpeakers(speakers);
        }
    }

    @Override
    public void onStop() {
        if (speakersSubscription != null) {
            speakersSubscription.unsubscribe();
        }
        super.onStop();
    }

    @Override
    public void reloadData() {
        loadData();
    }

    private void loadData() {
        speakersSubscription = dataProvider.getSpeakers()
                .doOnNext(speakers -> {
                    if (speakers != null) {
                        Collections.sort(speakers, (lhs, rhs) -> lhs.getName().compareTo(rhs.getName()));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(speakers -> {
                    this.speakers = new ArrayList<>(speakers);
                    view.displaySpeakers(speakers);
                }, throwable -> Timber.e(throwable, "Error getting speakers"), () -> {
                    if (speakers == null) {
                        view.displayLoadingError();
                    }
                });
    }
}
