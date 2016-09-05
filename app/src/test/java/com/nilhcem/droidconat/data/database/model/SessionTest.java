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
public class SessionTest {

    @Test
    public void should_create_content_values_for_sqlite() {
        // Given
        Session session = new Session(10, "startAt", 45, 3, "[3, 4]", "title", "description");

        // When
        ContentValues result = Session.createContentValues(session);

        // Then
        assertThat(result.get("_id")).isEqualTo(10);
        assertThat(result.get("start_at")).isEqualTo("startAt");
        assertThat(result.get("duration")).isEqualTo(45);
        assertThat(result.get("room_id")).isEqualTo(3);
        assertThat(result.get("speakers_ids")).isEqualTo("[3, 4]");
        assertThat(result.get("title")).isEqualTo("title");
        assertThat(result.get("description")).isEqualTo("description");
    }

    @Test
    public void should_create_object_from_sqlite() {
        // Given
        Cursor cursor = mock(Cursor.class);
        when(cursor.getColumnIndexOrThrow("_id")).thenReturn(1);
        when(cursor.getInt(1)).thenReturn(10);
        when(cursor.getColumnIndexOrThrow("start_at")).thenReturn(2);
        when(cursor.getString(2)).thenReturn("startAt");
        when(cursor.getColumnIndexOrThrow("duration")).thenReturn(3);
        when(cursor.getInt(3)).thenReturn(45);
        when(cursor.getColumnIndexOrThrow("room_id")).thenReturn(4);
        when(cursor.getInt(4)).thenReturn(3);
        when(cursor.getColumnIndexOrThrow("speakers_ids")).thenReturn(5);
        when(cursor.getString(5)).thenReturn("[3, 4]");
        when(cursor.getColumnIndexOrThrow("title")).thenReturn(6);
        when(cursor.getString(6)).thenReturn("title");
        when(cursor.getColumnIndexOrThrow("description")).thenReturn(7);
        when(cursor.getString(7)).thenReturn("description");

        // When
        Session result = Session.MAPPER.call(cursor);

        // Then
        assertThat(result.id).isEqualTo(10);
        assertThat(result.startAt).isEqualTo("startAt");
        assertThat(result.duration).isEqualTo(45);
        assertThat(result.speakersIds).isEqualTo("[3, 4]");
        assertThat(result.title).isEqualTo("title");
        assertThat(result.description).isEqualTo("description");
    }
}
