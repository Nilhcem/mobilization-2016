package com.nilhcem.droidconat.core.moshi;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocalDateTimeAdapter {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.US);

    @Inject
    public LocalDateTimeAdapter() {
    }

    @ToJson
    public String toText(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    @FromJson
    public LocalDateTime fromText(String text) {
        return LocalDateTime.parse(text, formatter);
    }
}
