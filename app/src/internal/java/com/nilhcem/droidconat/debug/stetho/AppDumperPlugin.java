package com.nilhcem.droidconat.debug.stetho;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;

import com.facebook.stetho.dumpapp.DumpException;
import com.facebook.stetho.dumpapp.DumperContext;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.nilhcem.droidconat.R;
import com.nilhcem.droidconat.data.app.model.Session;
import com.nilhcem.droidconat.data.database.dao.SessionsDao;
import com.nilhcem.droidconat.data.network.ApiEndpoint;
import com.nilhcem.droidconat.debug.lifecycle.ActivityProvider;
import com.nilhcem.droidconat.receiver.BootReceiver;
import com.nilhcem.droidconat.receiver.reminder.ReminderReceiver;
import com.nilhcem.droidconat.receiver.reminder.ReminderReceiverIntentBuilder;
import com.nilhcem.droidconat.ui.drawer.DrawerActivity;
import com.nilhcem.droidconat.ui.sessions.details.SessionDetailsActivity;
import com.nilhcem.droidconat.utils.App;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;

import org.threeten.bp.format.DateTimeFormatter;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import okio.Buffer;
import rx.Observable;

public class AppDumperPlugin implements DumperPlugin {

    private final Context context;
    private final ApiEndpoint endpoint;
    private final SessionsDao sessionsDao;
    private final ActivityProvider activityProvider;
    private final Moshi moshi;

    @Inject
    public AppDumperPlugin(Application app, ApiEndpoint endpoint, SessionsDao sessionsDao, ActivityProvider activityProvider, Moshi moshi) {
        this.context = app;
        this.endpoint = endpoint;
        this.sessionsDao = sessionsDao;
        this.activityProvider = activityProvider;
        this.moshi = moshi;
    }

    @Override
    public String getName() {
        return "droidconat";
    }

    @Override
    public void dump(DumperContext dumpContext) throws DumpException {
        final PrintStream writer = dumpContext.getStdout();
        List<String> args = dumpContext.getArgsAsList();
        String commandName = args.isEmpty() ? "" : args.remove(0);

        switch (commandName) {
            case "alarms":
                displayAlarms(writer);
                break;
            case "appInfo":
                displayAppInfo(writer);
                break;
            case "bootReceiver":
                displayBootReceiverState(writer);
                break;
            case "currentSession":
                displayCurrentSessionData(writer);
                break;
            case "endpoint":
                changeEndpoint(writer, args);
                break;
            case "notif":
                displayNotificationReminder();
                break;
            default:
                doUsage(writer);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void displayAlarms(PrintStream writer) {
        sessionsDao.getSessions()
                .flatMap(Observable::from)
                .map(session -> {
                    Intent intent = new ReminderReceiverIntentBuilder(session).build(context);
                    PendingIntent broadcast = PendingIntent.getBroadcast(context, session.getId(), intent, PendingIntent.FLAG_NO_CREATE);
                    if (broadcast != null) {
                        return String.format(Locale.US, "%s - Session(id=%d, title=%s)", session.getFromTime().format(DateTimeFormatter.ISO_DATE_TIME), session.getId(), session.getTitle());
                    }
                    return null;
                })
                .filter(id -> id != null)
                .toList()
                .subscribe(activeAlarms -> {
                    writer.println(Integer.toString(activeAlarms.size()) + " active alarm(s)");
                    for (String activeAlarm : activeAlarms) {
                        writer.println(activeAlarm);
                    }
                });
    }

    private void displayAppInfo(PrintStream writer) {
        writer.println(context.getString(R.string.app_name) + " " + App.getVersion());
    }

    private void displayBootReceiverState(PrintStream writer) {
        ComponentName componentName = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        writer.print("Boot receiver state: ");
        int state = pm.getComponentEnabledSetting(componentName);
        switch (state) {
            case PackageManager.COMPONENT_ENABLED_STATE_DEFAULT:
                writer.println("default");
                break;
            case PackageManager.COMPONENT_ENABLED_STATE_ENABLED:
                writer.println("enabled");
                break;
            case PackageManager.COMPONENT_ENABLED_STATE_DISABLED:
                writer.println("disabled");
                break;
            case PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER:
                writer.println("disabled by user");
                break;
            default:
                writer.println(state);
                break;
        }
    }

    private void displayCurrentSessionData(PrintStream writer) {
        Activity activity = activityProvider.getCurrentActivity();
        if (activity instanceof SessionDetailsActivity) {
            try {
                // Use reflection to access private "session" field
                Field field = SessionDetailsActivity.class.getDeclaredField("session");
                field.setAccessible(true);
                Session session = (Session) field.get(activity);

                // Convert sessions to a human readable json
                Buffer buffer = new Buffer();
                JsonWriter jsonWriter = JsonWriter.of(buffer);
                jsonWriter.setIndent("  ");
                moshi.adapter(Session.class).toJson(jsonWriter, session);
                String sessionJson = buffer.readUtf8();

                writer.println(sessionJson);
            } catch (Exception e) {
                writer.println(e.getMessage());
            }
        } else {
            writer.println("SessionDetailsActivity not visible");
        }
    }

    private void changeEndpoint(PrintStream writer, List<String> args) {
        if (args.size() < 1) {
            doUsage(writer);
        } else {
            switch (args.get(0)) {
                case "get":
                    writer.println(String.format(Locale.US, "Endpoint: %s", endpoint));
                    break;
                case "set":
                    if (args.size() < 2) {
                        doUsage(writer);
                    } else {
                        String arg = args.get(1);
                        try {
                            ApiEndpoint newEndpoint = ApiEndpoint.valueOf(arg.toUpperCase(Locale.US));
                            ApiEndpoint.persist(context, newEndpoint);
                        } catch (IllegalArgumentException e) {
                            ApiEndpoint.persist(context, arg);
                        }
                        restartApp(writer);
                    }
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private void displayNotificationReminder() {
        sessionsDao.getSessions()
                .flatMap(Observable::from)
                .filter(session -> session.getSpeakers() != null)
                .first()
                .subscribe(session -> {
                    Intent intent = new ReminderReceiverIntentBuilder(session).build(context);
                    new ReminderReceiver().onReceive(context, intent);
                });
    }

    private void doUsage(PrintStream writer) {
        writer.println("usage: dumpapp [arg]");
        writer.println();
        writer.println("arg:");
        writer.println("* alarms: Display AlarmManager active alarms");
        writer.println("* appInfo: Display current app build info");
        writer.println("* bootReceiver: Display boot receiver state");
        writer.println("* currentSession: Display current session data");
        writer.println("* endpoint get: Display current api endpoint");
        writer.println("* endpoint set (PROD|MOCK|\"https?://<url>\"): Change api endpoint");
        writer.println("* notif: Test a notification reminder");
    }

    private void restartApp(PrintStream writer) {
        writer.println("Restarting app...");

        // Restart app after a few delay to make sure stetho can print the previous message.
        new Thread(() -> {
            SystemClock.sleep(500);
            ProcessPhoenix.triggerRebirth(context, new Intent(context, DrawerActivity.class));
        }).start();
    }
}
