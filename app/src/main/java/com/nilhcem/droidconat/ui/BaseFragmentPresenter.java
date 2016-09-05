package com.nilhcem.droidconat.ui;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.view.View;

import icepick.Icepick;

public abstract class BaseFragmentPresenter<V> extends BasePresenter<V> {

    public BaseFragmentPresenter(V view) {
        super(view);
    }

    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Nothing to do by default
    }

    public void onStart() {
        // Nothing to do by default
    }

    public void onResume() {
        // Nothing to do by default
    }

    public void onStop() {
        // Nothing to do by default
    }

    @CallSuper
    public void onSaveInstanceState(Bundle outState) {
        Icepick.saveInstanceState(this, outState);
    }
}
