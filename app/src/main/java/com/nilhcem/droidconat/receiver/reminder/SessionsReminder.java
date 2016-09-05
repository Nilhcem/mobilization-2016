package com.nilhcem.droidconat.receiver.reminder;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.nilhcem.droidconat.R;
import com.nilhcem.droidconat.data.app.model.Session;
import com.nilhcem.droidconat.data.database.dao.SessionsDao;
import com.nilhcem.droidconat.utils.App;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneOffset;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Singleton
public class SessionsReminder {

    private final Context context;
    private final SessionsDao sessionsDao;
    private final SharedPreferences preferences;
    private final AlarmManager alarmManager;

    @Inject
    public SessionsReminder(Application app, SessionsDao sessionsDao, SharedPreferences preferences) {
        this.context = app;
        this.sessionsDao = sessionsDao;
        this.preferences = preferences;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public boolean isEnabled() {
        return preferences.getBoolean(context.getString(R.string.settings_notify_key), false);
    }

    public void enableSessionReminder() {
        performOnSelectedSessions(this::addSessionReminder);
    }

    public void disableSessionReminder() {
        performOnSelectedSessions(this::removeSessionReminder);
    }

    public void addSessionReminder(@NonNull Session session) {
        if (!isEnabled()) {
            Timber.d("SessionsReminder is not enable, skip adding session");
            return;
        }

        PendingIntent intent = createSessionReminderIntent(session);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sessionStartTime = session.getFromTime().minusMinutes(3);
        if (!sessionStartTime.isAfter(now)) {
            Timber.w("Do not set reminder for passed session");
            return;
        }
        Timber.d("Setting reminder on %s", sessionStartTime);
        App.setExactAlarm(alarmManager, sessionStartTime.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli(), intent);
    }

    public void removeSessionReminder(@NonNull Session session) {
        Timber.d("Cancelling reminder on %s", session.getFromTime().minusMinutes(3));
        createSessionReminderIntent(session).cancel();
    }

    private PendingIntent createSessionReminderIntent(@NonNull Session session) {
        Intent intent = new ReminderReceiverIntentBuilder(session).build(context);
        return PendingIntent.getBroadcast(context, session.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void performOnSelectedSessions(Action1<? super Session> onNext) {
        sessionsDao.getSelectedSessions()
                .flatMap(Observable::from)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(onNext, throwable -> Timber.e(throwable, "Error getting sessions"));
    }
}
