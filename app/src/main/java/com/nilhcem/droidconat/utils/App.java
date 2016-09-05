package com.nilhcem.droidconat.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Build;
import android.support.annotation.Nullable;

import com.nilhcem.droidconat.BuildConfig;
import com.nilhcem.droidconat.data.app.model.Session;
import com.nilhcem.droidconat.data.app.model.Speaker;

import java.util.List;
import java.util.Locale;

public final class App {

    private App() {
        throw new UnsupportedOperationException();
    }

    public static boolean isCompatible(int apiLevel) {
        return android.os.Build.VERSION.SDK_INT >= apiLevel;
    }

    public static String getVersion() {
        String version = String.format(Locale.US, "%s (#%d)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        if (BuildConfig.INTERNAL_BUILD) {
            version = String.format(Locale.US, "%s — commit %s", version, BuildConfig.GIT_SHA);
        }
        return version;
    }

    public static void setExactAlarm(AlarmManager alarmManager, long triggerAtMillis, PendingIntent operation) {
        if (isCompatible(Build.VERSION_CODES.KITKAT)) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, operation);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, operation);
        }
    }

    @Nullable
    public static String getPhotoUrl(@Nullable Session session) {
        String photoUrl = null;
        if (session != null) {
            List<Speaker> speakers = session.getSpeakers();
            if (speakers != null && !speakers.isEmpty()) {
                photoUrl = speakers.get(0).getPhoto();
            }
        }
        return photoUrl;
    }
}
