package com.nilhcem.droidconat.ui.sessions.details;

import android.os.Bundle;

import com.nilhcem.droidconat.R;
import com.nilhcem.droidconat.data.app.model.Session;
import com.nilhcem.droidconat.data.database.dao.SessionsDao;
import com.nilhcem.droidconat.receiver.reminder.SessionsReminder;
import com.nilhcem.droidconat.ui.BaseActivityPresenter;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SessionDetailsPresenter extends BaseActivityPresenter<SessionDetailsMvp.View> implements SessionDetailsMvp.Presenter {

    private final Session session;
    private final SessionsDao sessionsDao;
    private final SessionsReminder sessionsReminder;

    public SessionDetailsPresenter(SessionDetailsMvp.View view, Session session, SessionsDao sessionsDao, SessionsReminder sessionsReminder) {
        super(view);
        this.session = session;
        this.sessionsDao = sessionsDao;
        this.sessionsReminder = sessionsReminder;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        view.bindSessionDetails(session);
        sessionsDao.isSelected(session)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isSelected -> view.updateFabButton(isSelected, false),
                        throwable -> Timber.e(throwable, "Error getting selected session state"));
    }

    @Override
    public void onFloatingActionButtonClicked() {
        sessionsDao.getSelectedSessions(session.getFromTime())
                .map(sessions -> {
                    boolean shouldInsert = true;
                    for (Session curSession : sessions) {
                        sessionsReminder.removeSessionReminder(curSession);
                        if (curSession.getId() == session.getId()) {
                            shouldInsert = false;
                        }
                    }

                    if (shouldInsert) {
                        sessionsReminder.addSessionReminder(session);
                    }
                    sessionsDao.toggleSelectedSessionState(session, shouldInsert);
                    return shouldInsert;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(shouldInsert -> {
                    if (shouldInsert) {
                        view.showSnackbarMessage(R.string.session_details_added);
                    } else {
                        view.showSnackbarMessage(R.string.session_details_removed);
                    }
                    view.updateFabButton(shouldInsert, true);
                }, throwable -> Timber.e(throwable, "Error getting selected session state"));
    }
}
