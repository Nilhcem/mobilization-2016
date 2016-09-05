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
public class SelectedSessionTest {

    @Test
    public void should_create_content_values_for_sqlite() {
        // Given
        String slotTime = "22:07";
        int sessionId = 42;

        // When
        ContentValues result = new SelectedSession.Builder().slotTime(slotTime).sessionId(sessionId).build();

        // Then
        assertThat(result.get("slot_time")).isEqualTo(slotTime);
        assertThat(result.get("session_id")).isEqualTo(sessionId);
    }

    @Test
    public void should_create_object_from_sqlite() {
        // Given
        Cursor cursor = mock(Cursor.class);
        when(cursor.getColumnIndexOrThrow("slot_time")).thenReturn(1);
        when(cursor.getString(1)).thenReturn("13:37");
        when(cursor.getColumnIndexOrThrow("session_id")).thenReturn(2);
        when(cursor.getInt(2)).thenReturn(8);

        // When
        SelectedSession result = SelectedSession.MAPPER.call(cursor);

        // Then
        assertThat(result.slotTime).isEqualTo("13:37");
        assertThat(result.sessionId).isEqualTo(8);
    }
}
