package com.nilhcem.droidconat.data.app.model;

import android.support.annotation.NonNull;

public enum Room {

    NONE(0, ""),
    ROOM_1(1, "Room 1"),
    ROOM_2(2, "Room 2"),
    ROOM_3(3, "Room 3");

    public final int id;
    public final String label;

    Room(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public static Room getFromId(int id) {
        for (Room room : Room.values()) {
            if (room.id == id) {
                return room;
            }
        }
        return NONE;
    }

    public static Room getFromLabel(@NonNull String label) {
        for (Room room : Room.values()) {
            if (label.equals(room.label)) {
                return room;
            }
        }
        return NONE;
    }
}
