package com.nilhcem.droidconat.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;

import icepick.Icepick;

public abstract class BaseActivityPresenter<V> extends BasePresenter<V> {

    public BaseActivityPresenter(V view) {
        super(view);
    }

    public void onPostCreate(Bundle savedInstanceState) {
        // Nothing to do by default
    }

    public void onResume() {
        // Nothing to do by default
    }

    @CallSuper
    public void onSaveInstanceState(Bundle outState) {
        Icepick.saveInstanceState(this, outState);
    }

    @CallSuper
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    public void onNavigationItemSelected(@IdRes int itemId) {
        // Nothing to do by default
    }

    public boolean onBackPressed() {
        return false;
    }
}
