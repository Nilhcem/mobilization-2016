package com.nilhcem.droidconat.debug.stetho;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.InspectorModulesProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.rhino.JsRuntimeReplFactoryBuilder;
import com.facebook.stetho.timber.StethoTree;
import com.nilhcem.droidconat.debug.lifecycle.ActivityProvider;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Scriptable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class StethoInitializer implements DumperPluginsProvider {

    private final Context context;
    private final AppDumperPlugin appDumper;
    private final ActivityProvider activityProvider;

    @Inject
    public StethoInitializer(Application application, AppDumperPlugin appDumper, ActivityProvider activityProvider) {
        this.context = application;
        this.appDumper = appDumper;
        this.activityProvider = activityProvider;
    }

    public void init() {
        Timber.plant(new StethoTree());
        Stetho.initialize(
                Stetho.newInitializerBuilder(context)
                        .enableDumpapp(this)
                        .enableWebKitInspector(createWebkitModulesProvider())
                        .build());
    }

    @Override
    public Iterable<DumperPlugin> get() {
        List<DumperPlugin> plugins = new ArrayList<>();
        for (DumperPlugin plugin : Stetho.defaultDumperPluginsProvider(context).get()) {
            plugins.add(plugin);
        }
        plugins.add(appDumper);
        return plugins;
    }

    private InspectorModulesProvider createWebkitModulesProvider() {
        return () -> new Stetho.DefaultInspectorModulesBuilder(context).runtimeRepl(
                new JsRuntimeReplFactoryBuilder(context)
                        .addFunction("activity", new BaseFunction() {
                            @Override
                            public Object call(org.mozilla.javascript.Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
                                return activityProvider.getCurrentActivity();
                            }
                        }).build()
        ).finish();
    }
}
