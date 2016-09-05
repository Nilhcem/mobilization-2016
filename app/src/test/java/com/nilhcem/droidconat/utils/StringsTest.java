package com.nilhcem.droidconat.utils;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class StringsTest {

    @Test
    public void should_capitalize_first_letter() {
        // Given
        String src = "hello";

        // When
        String result = Strings.capitalizeFirstLetter(src);

        // Then
        assertThat(result).isEqualTo("Hello");
    }
}
