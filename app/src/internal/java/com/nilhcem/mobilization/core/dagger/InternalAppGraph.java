package com.nilhcem.mobilization.core.dagger;

import com.nilhcem.mobilization.InternalMobilizationApp;

public interface InternalAppGraph extends AppGraph {

    void inject(InternalMobilizationApp app);
}
