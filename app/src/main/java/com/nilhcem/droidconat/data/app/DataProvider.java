package com.nilhcem.droidconat.data.app;

import com.nilhcem.droidconat.data.app.model.Schedule;
import com.nilhcem.droidconat.data.app.model.Session;
import com.nilhcem.droidconat.data.app.model.Speaker;
import com.nilhcem.droidconat.data.database.dao.SessionsDao;
import com.nilhcem.droidconat.data.database.dao.SpeakersDao;
import com.nilhcem.droidconat.data.network.DroidconService;
import com.nilhcem.droidconat.data.network.NetworkMapper;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

@Singleton
public class DataProvider {

    private final AppMapper appMapper;
    private final NetworkMapper networkMapper;
    private final DroidconService service;
    private final SpeakersDao speakersDao;
    private final SessionsDao sessionsDao;
    private final DataProviderCache cache;

    @Inject
    public DataProvider(AppMapper appMapper, NetworkMapper networkMapper, DroidconService service, SpeakersDao speakersDao, SessionsDao sessionsDao) {
        this.appMapper = appMapper;
        this.networkMapper = networkMapper;
        this.service = service;
        this.speakersDao = speakersDao;
        this.sessionsDao = sessionsDao;
        this.cache = new DataProviderCache();
    }

    public Observable<Schedule> getSchedule() {
        sessionsDao.initSelectedSessionsMemory();
        return getSessions().map(appMapper::toSchedule);
    }

    public Observable<List<Speaker>> getSpeakers() {
        return Observable.create(subscriber -> speakersDao.getSpeakers().subscribe(speakers -> {
            if (!speakers.isEmpty()) {
                subscriber.onNext(speakers);
            }

            if (!subscriber.isUnsubscribed()) {
                getSpeakersFromNetwork(subscriber);
            }
            subscriber.onCompleted();
        }, subscriber::onError));
    }

    private void getSpeakersFromNetwork(Subscriber<? super List<Speaker>> subscriber) {
        List<Speaker> fromCache = cache.getSpeakers();
        if (fromCache == null) {
            service.loadSpeakers()
                    .map(networkMapper::toAppSpeakers)
                    .subscribe(speakers -> {
                        subscriber.onNext(speakers);
                        speakersDao.saveSpeakers(speakers);
                        cache.saveSpeakers(speakers);
                    }, throwable -> Timber.e(throwable, "Error getting speakers from network"));
        } else {
            subscriber.onNext(fromCache);
        }
    }

    private Observable<List<Session>> getSessions() {
        return Observable.create(subscriber -> sessionsDao.getSessions().subscribe(sessions -> {
            if (!sessions.isEmpty()) {
                subscriber.onNext(sessions);
            }

            if (!subscriber.isUnsubscribed()) {
                getSessionsFromNetwork(subscriber);
            }
            subscriber.onCompleted();
        }, subscriber::onError));
    }

    private void getSessionsFromNetwork(Subscriber<? super List<Session>> subscriber) {
        List<Session> fromCache = cache.getSessions();
        if (fromCache == null) {
            Observable.zip(
                    service.loadSessions(),
                    getSpeakers().last().map(appMapper::speakersToMap),
                    networkMapper::toAppSessions)
                    .subscribe(sessions -> {
                        subscriber.onNext(sessions);
                        sessionsDao.saveSessions(sessions);
                        cache.saveSessions(sessions);
                    }, throwable -> Timber.e(throwable, "Error getting sessions from network"));
        } else {
            subscriber.onNext(fromCache);
        }
    }
}
