package com.nilhcem.mobilization.core.rxjava;

import java.util.concurrent.TimeUnit;

import rx.plugins.RxJavaHooks;
import rx.schedulers.TestScheduler;

public class TestSchedulerProxy {

    private static final TestScheduler SCHEDULER = new TestScheduler();
    private static final TestSchedulerProxy INSTANCE = new TestSchedulerProxy();

    static {
        try {
            RxJavaHooks.setOnIOScheduler(current -> SCHEDULER);
            RxJavaHooks.setOnComputationScheduler(current -> SCHEDULER);
            RxJavaHooks.setOnNewThreadScheduler(current -> SCHEDULER);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Schedulers class already initialized. " +
                    "Ensure you always use the TestSchedulerProxy in unit tests.", e);
        }
    }

    public static TestSchedulerProxy get() {
        return INSTANCE;
    }

    public void advanceBy(long delayTime, TimeUnit unit) {
        SCHEDULER.advanceTimeBy(delayTime, unit);
    }
}
