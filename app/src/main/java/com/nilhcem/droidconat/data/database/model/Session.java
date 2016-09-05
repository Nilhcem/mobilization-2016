package com.nilhcem.droidconat.data.database.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.nilhcem.droidconat.utils.Database;

import rx.functions.Func1;

public class Session {

    public static final String TABLE = "sessions";

    public static final String ID = "_id";
    public static final String START_AT = "start_at";
    public static final String DURATION = "duration";
    public static final String ROOM_ID = "room_id";
    public static final String SPEAKERS_IDS = "speakers_ids";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";

    public final int id;
    public final String startAt;
    public final int duration;
    public final int roomId;
    public final String speakersIds;
    public final String title;
    public final String description;

    public static final Func1<Cursor, Session> MAPPER = cursor -> {
        int id = Database.getInt(cursor, ID);
        String startAt = Database.getString(cursor, START_AT);
        int duration = Database.getInt(cursor, DURATION);
        int roomId = Database.getInt(cursor, ROOM_ID);
        String speakersIds = Database.getString(cursor, SPEAKERS_IDS);
        String title = Database.getString(cursor, TITLE);
        String description = Database.getString(cursor, DESCRIPTION);
        return new Session(id, startAt, duration, roomId, speakersIds, title, description);
    };

    public Session(int id, String startAt, int duration, int roomId, String speakersIds, String title, String description) {
        this.id = id;
        this.startAt = startAt;
        this.duration = duration;
        this.roomId = roomId;
        this.speakersIds = speakersIds;
        this.title = title;
        this.description = description;
    }

    public static ContentValues createContentValues(Session session) {
        ContentValues values = new ContentValues();
        values.put(ID, session.id);
        values.put(START_AT, session.startAt);
        values.put(DURATION, session.duration);
        values.put(ROOM_ID, session.roomId);
        values.put(SPEAKERS_IDS, session.speakersIds);
        values.put(TITLE, session.title);
        values.put(DESCRIPTION, session.description);
        return values;
    }
}
