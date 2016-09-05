package com.nilhcem.droidconat.core.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class OkHttpModule {

    @Provides @Singleton OkHttpClient provideOkHttpClient(OkHttpClient.Builder builder) {
        return builder.build();
    }
}
