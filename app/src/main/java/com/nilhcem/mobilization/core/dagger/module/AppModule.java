package com.nilhcem.mobilization.core.dagger.module;

import android.app.Application;

import com.nilhcem.mobilization.MobilizationApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {

    private final MobilizationApp app;

    public AppModule(MobilizationApp app) {
        this.app = app;
    }

    @Provides @Singleton Application provideApplication() {
        return app;
    }
}
