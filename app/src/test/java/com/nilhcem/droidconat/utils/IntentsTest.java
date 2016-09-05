package com.nilhcem.droidconat.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;

import com.nilhcem.droidconat.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.res.builder.RobolectricPackageManager;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowResolveInfo;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP, packageName = "com.nilhcem.droidconat")
public class IntentsTest {

    private Activity activity;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(Activity.class);
    }

    @Test
    public void should_start_url_intent() {
        // Given
        String url = "geo:22.5430883,114.1043205";
        RobolectricPackageManager rpm = (RobolectricPackageManager) RuntimeEnvironment.application.getPackageManager();
        rpm.addResolveInfoForIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(url)), ShadowResolveInfo.newResolveInfo("", "", ""));

        // When
        boolean result = Intents.startUri(activity, url);

        // Then
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();

        assertThat(result).isTrue();
        assertThat(startedIntent.getAction()).isEqualTo(Intent.ACTION_VIEW);
        assertThat(startedIntent.getData().toString()).isEqualTo(url);
    }

    @Test
    public void should_return_false_when_nothing_on_the_device_can_open_the_uri() {
        // Given
        String url = "http://www.google.com";
        RobolectricPackageManager rpm = (RobolectricPackageManager) RuntimeEnvironment.application.getPackageManager();
        rpm.addResolveInfoForIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(url)), (ResolveInfo) null);

        // When
        boolean result = Intents.startUri(activity, url);

        // Then
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();

        assertThat(result).isFalse();
        assertThat(startedIntent).isNull();
    }

    @Test
    public void should_start_external_uri() {
        // Given
        String url = "http://www.google.com";
        RobolectricPackageManager rpm = (RobolectricPackageManager) RuntimeEnvironment.application.getPackageManager();
        rpm.addResolveInfoForIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(url)), ShadowResolveInfo.newResolveInfo("", "", ""));

        // When
        Intents.startExternalUrl(activity, url);

        // Then
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();

        assertThat(startedIntent.getAction()).isEqualTo(Intent.ACTION_VIEW);
        assertThat(startedIntent.getData().toString()).isEqualTo(url);
    }
}
