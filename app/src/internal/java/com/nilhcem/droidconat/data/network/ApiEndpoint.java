package com.nilhcem.droidconat.data.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nilhcem.droidconat.BuildConfig;
import com.nilhcem.droidconat.utils.Preconditions;

import lombok.ToString;

@ToString
public enum ApiEndpoint {

    PROD(BuildConfig.API_ENDPOINT),
    MOCK(BuildConfig.MOCK_ENDPOINT),
    CUSTOM(null);

    private static final String PREFS_NAME = "api_endpoint";
    private static final String PREFS_KEY_NAME = "name";
    private static final String PREFS_KEY_URL = "url";

    public String url;

    ApiEndpoint(String url) {
        this.url = url;
    }

    public static ApiEndpoint get(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String prefsName = prefs.getString(PREFS_KEY_NAME, null);
        if (prefsName != null) {
            ApiEndpoint endpoint = valueOf(prefsName);
            if (endpoint == CUSTOM) {
                endpoint.url = prefs.getString(PREFS_KEY_URL, null);
            }
            return endpoint;
        }
        return PROD;
    }

    public static void persist(Context context, @NonNull ApiEndpoint endpoint) {
        Preconditions.checkArgument(endpoint != CUSTOM);
        persistEndpoint(context, endpoint, null);
    }

    public static void persist(Context context, @NonNull String url) {
        persistEndpoint(context, CUSTOM, url);
    }

    @SuppressLint("CommitPrefEdits")
    private static void persistEndpoint(Context context, @NonNull ApiEndpoint endpoint, @Nullable String url) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(PREFS_KEY_NAME, endpoint.name());

        if (url == null) {
            editor.remove(PREFS_KEY_URL);
        } else {
            editor.putString(PREFS_KEY_URL, url);
        }

        editor.commit();
    }
}
