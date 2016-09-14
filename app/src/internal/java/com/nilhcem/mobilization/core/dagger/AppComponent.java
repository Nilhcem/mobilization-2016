package com.nilhcem.mobilization.core.dagger;

import com.nilhcem.mobilization.DroidconApp;
import com.nilhcem.mobilization.core.dagger.module.ApiModule;
import com.nilhcem.mobilization.core.dagger.module.AppModule;
import com.nilhcem.mobilization.core.dagger.module.DataModule;
import com.nilhcem.mobilization.core.dagger.module.DatabaseModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class, DataModule.class, DatabaseModule.class})
public interface AppComponent extends InternalAppGraph {

    /**
     * An initializer that creates the internal graph from an application.
     */
    final class Initializer {

        private Initializer() {
            throw new UnsupportedOperationException();
        }

        public static AppComponent init(DroidconApp app) {
            return DaggerAppComponent.builder()
                    .appModule(new AppModule(app))
                    .apiModule(new ApiModule())
                    .dataModule(new DataModule())
                    .databaseModule(new DatabaseModule())
                    .build();
        }
    }
}