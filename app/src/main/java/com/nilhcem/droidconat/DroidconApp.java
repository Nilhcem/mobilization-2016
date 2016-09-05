package com.nilhcem.droidconat;

import android.app.Application;
import android.content.Context;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.nilhcem.droidconat.core.dagger.AppComponent;

import hugo.weaving.DebugLog;
import timber.log.Timber;

@DebugLog
public class DroidconApp extends Application {

    private AppComponent component;

    public static DroidconApp get(Context context) {
        return (DroidconApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        initGraph();
        initLogger();
    }

    public AppComponent component() {
        return component;
    }

    private void initGraph() {
        component = AppComponent.Initializer.init(this);
    }

    private void initLogger() {
        Timber.plant(new Timber.DebugTree());
    }
}
