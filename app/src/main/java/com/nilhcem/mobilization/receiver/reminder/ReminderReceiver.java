package com.nilhcem.mobilization.receiver.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.nilhcem.mobilization.R;
import com.nilhcem.mobilization.data.app.model.Session;
import com.nilhcem.mobilization.ui.sessions.details.SessionDetailsActivity;
import com.nilhcem.mobilization.ui.sessions.details.SessionDetailsActivityIntentBuilder;
import com.nilhcem.mobilization.utils.App;
import com.nilhcem.mobilization.utils.Downloader;
import com.nilhcem.mobilization.utils.Preconditions;

import rx.schedulers.Schedulers;
import se.emilsjolander.intentbuilder.Extra;
import se.emilsjolander.intentbuilder.IntentBuilder;
import timber.log.Timber;

@IntentBuilder
public class ReminderReceiver extends BroadcastReceiver {

    @Extra Session session;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("Received session reminder");
        ReminderReceiverIntentBuilder.inject(intent, this);
        Preconditions.checkArgument(session != null);
        showNotification(context, session);
    }

    private void showNotification(Context context, Session session) {
        Schedulers.io().createWorker().schedule(() -> {
            PendingIntent pendingIntent = TaskStackBuilder.create(context)
                    .addParentStack(SessionDetailsActivity.class)
                    .addNextIntent(new SessionDetailsActivityIntentBuilder(session).build(context))
                    .getPendingIntent(session.getId(), PendingIntent.FLAG_UPDATE_CURRENT);

            android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.reminder_about_to_start, session.getTitle())))
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true);

            String url = App.getPhotoUrl(session);
            if (!TextUtils.isEmpty(url)) {
                Bitmap bitmap = Downloader.downloadBitmap(url);
                if (bitmap != null) {
                    builder.setLargeIcon(bitmap);
                }
            }

            Notification notification = builder.build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(session.getId(), notification);
        });
    }
}
