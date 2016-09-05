package com.nilhcem.droidconat.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nilhcem.droidconat.data.database.model.SelectedSession;
import com.nilhcem.droidconat.data.database.model.Session;
import com.nilhcem.droidconat.data.database.model.Speaker;

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String NAME = "droidcon.db";
    private static final int VERSION = 1;

    public DbOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createSpeakersTable(db);
        createSessionsTable(db);
        createSelectedSessionsTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void createSpeakersTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Speaker.TABLE + " (" +
                Speaker.ID + " INTEGER PRIMARY KEY," +
                Speaker.NAME + " VARCHAR," +
                Speaker.TITLE + " VARCHAR," +
                Speaker.BIO + " VARCHAR," +
                Speaker.WEBSITE + " VARCHAR," +
                Speaker.TWITTER + " VARCHAR," +
                Speaker.GITHUB + " VARCHAR," +
                Speaker.PHOTO + " VARCHAR);");
    }

    private void createSessionsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Session.TABLE + " (" +
                Session.ID + " INTEGER PRIMARY KEY," +
                Session.START_AT + " VARCHAR," +
                Session.DURATION + " INTEGER," +
                Session.ROOM_ID + " INTEGER," +
                Session.SPEAKERS_IDS + " VARCHAR," +
                Session.TITLE + " VARCHAR," +
                Session.DESCRIPTION + " VARCHAR);");
    }

    private void createSelectedSessionsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + SelectedSession.TABLE + " (" +
                SelectedSession.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SelectedSession.SLOT_TIME + " VARCHAR," +
                SelectedSession.SESSION_ID + " INTEGER);");
    }
}
