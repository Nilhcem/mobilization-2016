package com.nilhcem.mobilization.core.dagger;

import com.nilhcem.mobilization.InternalDroidconApp;

public interface InternalAppGraph extends AppGraph {

    void inject(InternalDroidconApp app);
}
