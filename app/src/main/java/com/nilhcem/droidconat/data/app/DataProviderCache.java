package com.nilhcem.droidconat.data.app;

import com.nilhcem.droidconat.data.app.model.Session;
import com.nilhcem.droidconat.data.app.model.Speaker;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import timber.log.Timber;

public class DataProviderCache {

    private static final long CACHE_DURATION_MN = 10;

    List<Session> sessions;
    LocalDateTime sessionsFetchedTime;
    List<Speaker> speakers;
    LocalDateTime speakersFetchedTime;

    public List<Session> getSessions() {
        if (sessionsFetchedTime != null && sessionsFetchedTime.plusMinutes(CACHE_DURATION_MN).isAfter(LocalDateTime.now())) {
            Timber.d("Get sessions from cache");
            return sessions;
        }
        return null;
    }

    public void saveSessions(List<Session> sessions) {
        this.sessions = sessions;
        sessionsFetchedTime = LocalDateTime.now();
    }

    public List<Speaker> getSpeakers() {
        if (speakersFetchedTime != null && speakersFetchedTime.plusMinutes(CACHE_DURATION_MN).isAfter(LocalDateTime.now())) {
            Timber.d("Get speakers from cache");
            return speakers;
        }
        return null;
    }

    public void saveSpeakers(List<Speaker> speakers) {
        this.speakers = speakers;
        speakersFetchedTime = LocalDateTime.now();
    }
}
