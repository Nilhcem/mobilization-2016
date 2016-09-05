package com.nilhcem.droidconat.data.database.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;

import com.nilhcem.droidconat.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class SpeakerTest {

    @Test
    public void should_create_content_values_for_sqlite() {
        // Given
        Speaker speaker = new Speaker(3, "name", "title", "bio", "website", "twitter", "github", "photo");

        // When
        ContentValues result = Speaker.createContentValues(speaker);

        // Then
        assertThat(result.get("_id")).isEqualTo(3);
        assertThat(result.get("name")).isEqualTo("name");
        assertThat(result.get("title")).isEqualTo("title");
        assertThat(result.get("bio")).isEqualTo("bio");
        assertThat(result.get("website")).isEqualTo("website");
        assertThat(result.get("twitter")).isEqualTo("twitter");
        assertThat(result.get("github")).isEqualTo("github");
        assertThat(result.get("photo")).isEqualTo("photo");
    }

    @Test
    public void should_create_object_from_sqlite() {
        // Given
        Cursor cursor = mock(Cursor.class);
        when(cursor.getColumnIndexOrThrow("_id")).thenReturn(1);
        when(cursor.getInt(1)).thenReturn(3);
        when(cursor.getColumnIndexOrThrow("name")).thenReturn(2);
        when(cursor.getString(2)).thenReturn("name");
        when(cursor.getColumnIndexOrThrow("title")).thenReturn(3);
        when(cursor.getString(3)).thenReturn("title");
        when(cursor.getColumnIndexOrThrow("bio")).thenReturn(4);
        when(cursor.getString(4)).thenReturn("bio");
        when(cursor.getColumnIndexOrThrow("website")).thenReturn(5);
        when(cursor.getString(5)).thenReturn("website");
        when(cursor.getColumnIndexOrThrow("twitter")).thenReturn(6);
        when(cursor.getString(6)).thenReturn("twitter");
        when(cursor.getColumnIndexOrThrow("github")).thenReturn(7);
        when(cursor.getString(7)).thenReturn("github");
        when(cursor.getColumnIndexOrThrow("photo")).thenReturn(8);
        when(cursor.getString(8)).thenReturn("photo");

        // When
        Speaker result = Speaker.MAPPER.call(cursor);

        // Then
        assertThat(result.id).isEqualTo(3);
        assertThat(result.name).isEqualTo("name");
        assertThat(result.title).isEqualTo("title");
        assertThat(result.bio).isEqualTo("bio");
        assertThat(result.website).isEqualTo("website");
        assertThat(result.twitter).isEqualTo("twitter");
        assertThat(result.github).isEqualTo("github");
        assertThat(result.photo).isEqualTo("photo");
    }
}
