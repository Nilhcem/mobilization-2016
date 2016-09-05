package com.nilhcem.droidconat.utils;

import android.database.Cursor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest {

    @Mock Cursor cursor;

    @Test
    public void should_return_string_cursor_data() {
        // Given
        when(cursor.getColumnIndexOrThrow("column_name")).thenReturn(42);
        when(cursor.getString(42)).thenReturn("string-data");

        // When
        String result = Database.getString(cursor, "column_name");

        // Then
        assertThat(result).isEqualTo("string-data");
    }

    @Test
    public void should_return_int_cursor_data() {
        // Given
        when(cursor.getColumnIndexOrThrow("column_name")).thenReturn(21);
        when(cursor.getInt(21)).thenReturn(10);

        // When
        int result = Database.getInt(cursor, "column_name");

        // Then
        assertThat(result).isEqualTo(10);
    }
}
