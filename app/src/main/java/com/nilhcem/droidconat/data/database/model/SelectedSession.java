package com.nilhcem.droidconat.data.database.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.nilhcem.droidconat.utils.Database;

import rx.functions.Func1;

public class SelectedSession {

    public static final String TABLE = "selected_sessions";

    public static final String ID = "_id";
    public static final String SLOT_TIME = "slot_time";
    public static final String SESSION_ID = "session_id";

    public static final Func1<Cursor, SelectedSession> MAPPER = cursor -> {
        String slotTime = Database.getString(cursor, SLOT_TIME);
        int sessionId = Database.getInt(cursor, SESSION_ID);
        return new SelectedSession(slotTime, sessionId);
    };

    public final String slotTime;
    public final int sessionId;

    public SelectedSession(String slotTime, int sessionId) {
        this.slotTime = slotTime;
        this.sessionId = sessionId;
    }

    public static final class Builder {

        private final ContentValues values = new ContentValues();

        public Builder slotTime(String slotTime) {
            values.put(SLOT_TIME, slotTime);
            return this;
        }

        public Builder sessionId(int sessionId) {
            values.put(SESSION_ID, sessionId);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
