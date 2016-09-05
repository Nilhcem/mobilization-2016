package com.nilhcem.droidconat.ui.drawer;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

public interface DrawerMvp {

    interface View {
        boolean isNavigationDrawerOpen();

        void closeNavigationDrawer();

        void updateToolbarTitle(@StringRes int resId);

        void showFragment(Fragment fragment);

        void selectDrawerMenuItem(@IdRes int id);

        void hideTabLayout();
    }

    interface Presenter {
        void onNavigationItemSelected(@IdRes int itemId);
    }
}
