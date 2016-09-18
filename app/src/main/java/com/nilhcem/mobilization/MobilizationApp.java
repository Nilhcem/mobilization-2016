package com.nilhcem.mobilization;

import android.app.Application;
import android.content.Context;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.nilhcem.mobilization.core.dagger.AppComponent;

import hugo.weaving.DebugLog;
import timber.log.Timber;

@DebugLog
public class MobilizationApp extends Application {

    private AppComponent component;

    public static MobilizationApp get(Context context) {
        return (MobilizationApp) context.getApplicationContext();
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
