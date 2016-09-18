package com.nilhcem.mobilization.data.network;

import com.nilhcem.mobilization.data.network.model.Session;
import com.nilhcem.mobilization.data.network.model.Speaker;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface MobilizationService {

    @GET("sessions")
    Observable<List<Session>> loadSessions();

    @GET("speakers")
    Observable<List<Speaker>> loadSpeakers();
}
