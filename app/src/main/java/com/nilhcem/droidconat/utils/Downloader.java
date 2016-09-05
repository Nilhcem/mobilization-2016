package com.nilhcem.droidconat.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public final class Downloader {

    private Downloader() {
        throw new UnsupportedOperationException();
    }

    public static Bitmap downloadBitmap(@NonNull String url) {
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(url)) {
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = new OkHttpClient.Builder().build().newCall(request).execute();
                InputStream inputStream = response.body().byteStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                Timber.e(e, "Error getting bitmap for url: %s", url);
            }
        }
        return bitmap;
    }
}
