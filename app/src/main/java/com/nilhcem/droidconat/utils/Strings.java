package com.nilhcem.droidconat.utils;

import android.support.annotation.NonNull;

import java.util.Locale;

public class Strings {

    private Strings() {
        throw new UnsupportedOperationException();
    }

    public static String capitalizeFirstLetter(@NonNull String src) {
        return src.substring(0, 1).toUpperCase(Locale.getDefault()) + src.substring(1);
    }
}
