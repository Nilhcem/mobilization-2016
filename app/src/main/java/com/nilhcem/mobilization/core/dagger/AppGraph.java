package com.nilhcem.mobilization.core.dagger;

import com.nilhcem.mobilization.receiver.BootReceiver;
import com.nilhcem.mobilization.receiver.reminder.ReminderReceiver;
import com.nilhcem.mobilization.ui.drawer.DrawerActivity;
import com.nilhcem.mobilization.ui.schedule.day.ScheduleDayFragment;
import com.nilhcem.mobilization.ui.schedule.pager.SchedulePagerFragment;
import com.nilhcem.mobilization.ui.sessions.details.SessionDetailsActivity;
import com.nilhcem.mobilization.ui.sessions.list.SessionsListActivity;
import com.nilhcem.mobilization.ui.settings.SettingsFragment;
import com.nilhcem.mobilization.ui.speakers.details.SpeakerDetailsDialogFragment;
import com.nilhcem.mobilization.ui.speakers.list.SpeakersListFragment;

/**
 * A common interface implemented by both the internal and production flavored components.
 */
public interface AppGraph {

    void inject(DrawerActivity activity);

    void inject(SchedulePagerFragment fragment);

    void inject(ScheduleDayFragment fragment);

    void inject(SessionsListActivity activity);

    void inject(SpeakersListFragment fragments);

    void inject(SessionDetailsActivity activity);

    void inject(SpeakerDetailsDialogFragment fragment);

    void inject(SettingsFragment fragment);

    void inject(BootReceiver receiver);

    void inject(ReminderReceiver receiver);
}
