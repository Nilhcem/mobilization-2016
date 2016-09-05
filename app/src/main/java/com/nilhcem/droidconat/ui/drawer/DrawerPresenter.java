package com.nilhcem.droidconat.ui.drawer;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

import com.nilhcem.droidconat.R;
import com.nilhcem.droidconat.ui.BaseActivityPresenter;
import com.nilhcem.droidconat.ui.schedule.pager.SchedulePagerFragmentBuilder;
import com.nilhcem.droidconat.ui.settings.SettingsFragment;
import com.nilhcem.droidconat.ui.speakers.list.SpeakersListFragment;
import com.nilhcem.droidconat.ui.venue.VenueFragment;

import icepick.State;

public class DrawerPresenter extends BaseActivityPresenter<DrawerMvp.View> implements DrawerMvp.Presenter {

    @State @StringRes int toolbarTitle;
    @State @IdRes int selectedItemId;

    public DrawerPresenter(DrawerMvp.View view) {
        super(view);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState == null) {
            onNavigationItemSelected(R.id.drawer_nav_schedule);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        view.updateToolbarTitle(toolbarTitle);
    }

    @Override
    public void onNavigationItemSelected(@IdRes int itemId) {
        if (itemId != selectedItemId) {
            switch (itemId) {
                case R.id.drawer_nav_schedule:
                    view.showFragment(new SchedulePagerFragmentBuilder(false).build());
                    toolbarTitle = R.string.drawer_nav_schedule;
                    break;
                case R.id.drawer_nav_agenda:
                    view.showFragment(new SchedulePagerFragmentBuilder(true).build());
                    toolbarTitle = R.string.drawer_nav_agenda;
                    break;
                case R.id.drawer_nav_speakers:
                    view.showFragment(new SpeakersListFragment());
                    toolbarTitle = R.string.drawer_nav_speakers;
                    break;
                case R.id.drawer_nav_venue:
                    view.showFragment(new VenueFragment());
                    toolbarTitle = R.string.drawer_nav_venue;
                    break;
                case R.id.drawer_nav_settings:
                    view.showFragment(new SettingsFragment());
                    toolbarTitle = R.string.drawer_nav_settings;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            view.hideTabLayout();
            view.updateToolbarTitle(toolbarTitle);

            selectedItemId = itemId;
        }
        view.closeNavigationDrawer();
    }

    @Override
    public boolean onBackPressed() {
        if (view.isNavigationDrawerOpen()) {
            view.closeNavigationDrawer();
            return true;
        } else if (toolbarTitle != R.string.drawer_nav_schedule) {
            int firstItem = R.id.drawer_nav_schedule;
            onNavigationItemSelected(firstItem);
            view.selectDrawerMenuItem(firstItem);
            return true;
        }
        return false;
    }
}
