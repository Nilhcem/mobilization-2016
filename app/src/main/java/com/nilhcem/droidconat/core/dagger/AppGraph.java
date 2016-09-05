package com.nilhcem.droidconat.core.dagger;

import com.nilhcem.droidconat.receiver.BootReceiver;
import com.nilhcem.droidconat.ui.drawer.DrawerActivity;
import com.nilhcem.droidconat.ui.schedule.day.ScheduleDayFragment;
import com.nilhcem.droidconat.ui.schedule.pager.SchedulePagerFragment;
import com.nilhcem.droidconat.ui.sessions.details.SessionDetailsActivity;
import com.nilhcem.droidconat.ui.sessions.list.SessionsListActivity;
import com.nilhcem.droidconat.ui.settings.SettingsFragment;
import com.nilhcem.droidconat.ui.speakers.details.SpeakerDetailsDialogFragment;
import com.nilhcem.droidconat.ui.speakers.list.SpeakersListFragment;

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
}
