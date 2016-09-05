package com.nilhcem.droidconat.core.dagger;

import com.nilhcem.droidconat.InternalDroidconApp;

public interface InternalAppGraph extends AppGraph {

    void inject(InternalDroidconApp app);
}
