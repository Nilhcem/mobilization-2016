package com.nilhcem.droidconat.core.dagger.module;

import android.app.Application;

import com.nilhcem.droidconat.data.network.ApiEndpoint;
import com.nilhcem.droidconat.data.network.DroidconService;
import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public final class ApiModule {

    @Provides @Singleton ApiEndpoint provideApiEndpoint(Application context) {
        return ApiEndpoint.get(context);
    }

    @Provides @Singleton Retrofit provideRetrofit(OkHttpClient client, Moshi moshi, ApiEndpoint endpoint) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(endpoint.url)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides @Singleton
    DroidconService provideDroidconService(Retrofit retrofit) {
        return retrofit.create(DroidconService.class);
    }
}
