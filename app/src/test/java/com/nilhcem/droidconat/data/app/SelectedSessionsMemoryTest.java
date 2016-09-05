package com.nilhcem.droidconat.data.app;

import android.os.Build;

import com.nilhcem.droidconat.BuildConfig;
import com.nilhcem.droidconat.data.app.model.Session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.threeten.bp.LocalDateTime;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class SelectedSessionsMemoryTest {

    private final SelectedSessionsMemory memory = new SelectedSessionsMemory();

    @Test
    public void should_set_selected_sessions() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Map<LocalDateTime, Integer> map = new HashMap<>();
        map.put(now, 1);

        // When
        assertThat(memory.get(now)).isNull();
        memory.setSelectedSessions(map);

        // Then
        assertThat(memory.get(now)).isEqualTo(1);
    }

    @Test
    public void should_remove_previous_session_when_adding_a_new_one_for_the_same_slot_time() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Map<LocalDateTime, Integer> map = new HashMap<>();
        map.put(now, 1);
        memory.setSelectedSessions(map);
        Session toAdd = new Session(3, null, null, null, null, now, now.plusMinutes(30));

        // When
        assertThat(memory.get(now)).isEqualTo(1);
        memory.toggleSessionState(toAdd, true);

        // Then
        assertThat(memory.get(now)).isEqualTo(3);
    }
}
