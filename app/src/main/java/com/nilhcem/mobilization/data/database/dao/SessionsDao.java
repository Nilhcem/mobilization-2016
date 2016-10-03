package com.nilhcem.mobilization.data.database.dao;

import android.database.Cursor;

import com.nilhcem.mobilization.core.moshi.LocalDateTimeAdapter;
import com.nilhcem.mobilization.data.app.SelectedSessionsMemory;
import com.nilhcem.mobilization.data.database.DbMapper;
import com.nilhcem.mobilization.data.database.model.SelectedSession;
import com.nilhcem.mobilization.data.database.model.Session;
import com.nilhcem.mobilization.utils.Preconditions;
import com.squareup.sqlbrite.BriteDatabase;

import org.threeten.bp.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Singleton
public class SessionsDao {

    private final BriteDatabase database;
    private final DbMapper dbMapper;
    private final SpeakersDao speakersDao;
    private final LocalDateTimeAdapter adapter;
    private final SelectedSessionsMemory selectedSessionsMemory;

    @Inject
    public SessionsDao(BriteDatabase database, DbMapper dbMapper, SpeakersDao speakersDao, LocalDateTimeAdapter adapter, SelectedSessionsMemory selectedSessionsMemory) {
        this.database = database;
        this.dbMapper = dbMapper;
        this.speakersDao = speakersDao;
        this.adapter = adapter;
        this.selectedSessionsMemory = selectedSessionsMemory;
    }

    public Observable<List<com.nilhcem.mobilization.data.app.model.Session>> getSessions() {
        return getSessions("SELECT * FROM " + Session.TABLE);
    }

    public Observable<com.nilhcem.mobilization.data.app.model.Session> getSessionById(int id) {
        return getSessions("SELECT * FROM " + Session.TABLE + " WHERE " + Session.ID + "=?", Integer.toString(id)).flatMap(Observable::from).first();
    }

    public Observable<List<com.nilhcem.mobilization.data.app.model.Session>> getSelectedSessions() {
        String query = String.format(Locale.US, "SELECT %s.* FROM %s INNER JOIN %s ON %s.%s=%s.%s",
                Session.TABLE, Session.TABLE, SelectedSession.TABLE, Session.TABLE, Session.ID, SelectedSession.TABLE, SelectedSession.SESSION_ID);
        return getSessions(query);
    }

    public Observable<List<com.nilhcem.mobilization.data.app.model.Session>> getSelectedSessions(LocalDateTime slotTime) {
        String query = String.format(Locale.US, "SELECT %s.* FROM %s INNER JOIN %s ON %s.%s=%s.%s WHERE %s=?",
                Session.TABLE, Session.TABLE, SelectedSession.TABLE, Session.TABLE, Session.ID, SelectedSession.TABLE, SelectedSession.SESSION_ID, SelectedSession.SLOT_TIME);
        return getSessions(query, adapter.toText(slotTime));
    }

    public void toggleSelectedSessionState(com.nilhcem.mobilization.data.app.model.Session session, boolean insert) {
        Preconditions.checkNotOnMainThread();

        String slotTime = adapter.toText(session.getFromTime());
        BriteDatabase.Transaction transaction = database.newTransaction();
        try {
            database.delete(SelectedSession.TABLE, SelectedSession.SLOT_TIME + "=?", slotTime);
            if (insert) {
                database.insert(SelectedSession.TABLE, new SelectedSession.Builder().slotTime(slotTime).sessionId(session.getId()).build());
            }
            selectedSessionsMemory.toggleSessionState(session, insert);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    public void saveSessions(List<com.nilhcem.mobilization.data.app.model.Session> toSave) {
        Preconditions.checkNotOnMainThread();

        BriteDatabase.Transaction transaction = database.newTransaction();
        try {
            database.delete(Session.TABLE, null);
            for (com.nilhcem.mobilization.data.app.model.Session session : toSave) {
                database.insert(Session.TABLE, Session.createContentValues(dbMapper.fromAppSession(session)));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    public Observable<Boolean> isSelected(com.nilhcem.mobilization.data.app.model.Session session) {
        return database.createQuery(SelectedSession.TABLE, "SELECT " + SelectedSession.SESSION_ID +
                " FROM " + SelectedSession.TABLE + " WHERE " + SelectedSession.SESSION_ID + "=?", Integer.toString(session.getId()))
                .mapToOneOrDefault(Cursor::getCount, 0)
                .first()
                .map(it -> it > 0);
    }

    public void initSelectedSessionsMemory() {
        database.createQuery(SelectedSession.TABLE, "SELECT * FROM " + SelectedSession.TABLE)
                .mapToList(SelectedSession.MAPPER)
                .first()
                .map(selectedSessions -> {
                    Map<LocalDateTime, Integer> sessions = new HashMap<>();
                    for (SelectedSession session : selectedSessions) {
                        sessions.put(adapter.fromText(session.slotTime), session.sessionId);
                    }
                    return sessions;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(selectedSessionsMemory::setSelectedSessions);
    }

    private Observable<List<com.nilhcem.mobilization.data.app.model.Session>> getSessions(String query, String... args) {
        return Observable.zip(
                database.createQuery(Session.TABLE, query, args).mapToList(Session.MAPPER).first(),
                speakersDao.getSpeakersMap(),
                dbMapper::toAppSessions);
    }
}
