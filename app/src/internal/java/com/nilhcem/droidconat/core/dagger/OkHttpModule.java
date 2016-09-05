package com.nilhcem.droidconat.core.dagger;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class OkHttpModule {

    @Provides @Singleton OkHttpClient provideOkHttpClient(OkHttpClient.Builder builder) {
        return builder.addNetworkInterceptor(new StethoInterceptor()).build();
    }
}
