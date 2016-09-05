package com.nilhcem.droidconat.core.log;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class CrashFsPersister implements Thread.UncaughtExceptionHandler {

    private static final String LOG_FILE = "errors.log";

    private final Context context;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM HH:mm:ss.SS", Locale.US);
    private final Thread.UncaughtExceptionHandler handler;

    private File logFile;

    public CrashFsPersister(Context context) {
        this.context = context;
        handler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        try {
            File logFile = getLogFileLazy();
            if (logFile != null) {
                StringWriter sw = new StringWriter();
                throwable.printStackTrace(new PrintWriter(sw));

                PrintWriter out = null;
                try {
                    out = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
                    out.println(String.format(Locale.US, "%s: %s", dateFormat.format(new Date()), sw.toString()));
                } catch (IOException e) {
                    Timber.e(e, "Error saving uncaught exception");
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            }
        } finally {
            handler.uncaughtException(thread, throwable);
        }
    }

    private File getLogFileLazy() {
        if (logFile == null) {
            logFile = new File(ContextCompat.getExternalCacheDirs(context)[0], LOG_FILE);
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    Timber.e(e, "Error creating new file");
                }
            }
        }
        return logFile;
    }
}
