package com.nilhcem.droidconat.data.app;

import android.os.Build;

import com.nilhcem.droidconat.BuildConfig;
import com.nilhcem.droidconat.data.app.model.Schedule;
import com.nilhcem.droidconat.data.app.model.Session;
import com.nilhcem.droidconat.data.app.model.Speaker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.threeten.bp.LocalDateTime;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Collections.singletonList;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class AppMapperTest {

    private final AppMapper appMapper = new AppMapper();
    private final Speaker speaker1 = new Speaker(10, "Gautier", null, null, null, null, null, null);
    private final Speaker speaker2 = new Speaker(20, "Mounir", null, null, null, null, null, null);
    private final Speaker speaker3 = new Speaker(30, "Thomas", null, null, null, null, null, null);
    private final Speaker speaker4 = new Speaker(40, "Arnaud", null, null, null, null, null, null);
    private final Speaker speaker5 = new Speaker(50, "Jérémie", null, null, null, null, null, null);
    private final List<Speaker> speakers = Arrays.asList(speaker1, speaker2, speaker3, speaker4, speaker5);

    @Test
    public void should_create_map_of_speakers_with_id_as_speakerId_and_value_as_speaker() {
        // Given When
        Map<Integer, Speaker> map = appMapper.speakersToMap(speakers);

        // Then
        assertThat(map.get(10)).isEqualTo(speaker1);
        assertThat(map.get(20)).isEqualTo(speaker2);
        assertThat(map.get(30)).isEqualTo(speaker3);
        assertThat(map.get(40)).isEqualTo(speaker4);
        assertThat(map.get(50)).isEqualTo(speaker5);
        assertThat(map).doesNotContainKey(8);
    }

    @Test
    public void should_create_speakers_list_when_giving_ids() {
        // Given
        List<Integer> ids = Arrays.asList(10, 20, 40);

        // When
        List<Speaker> result = appMapper.toSpeakersList(ids, appMapper.speakersToMap(this.speakers));

        // Then
        assertThat(result).hasSize(3);
        assertThat(result).containsAllOf(speaker1, speaker2, speaker4);
    }

    @Test
    public void should_return_list_of_ids() {
        // Given
        List<Speaker> speakers = Arrays.asList(speaker3, speaker5);

        // When
        List<Integer> result = appMapper.toSpeakersIds(speakers);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsAllOf(30, 50);
    }

    @Test
    public void should_convert_sessions_list_into_schedule() {
        // Given
        LocalDateTime slot1 = LocalDateTime.of(2016, 3, 15, 8, 10);
        LocalDateTime slot2 = slot1.plusHours(1);
        LocalDateTime slot3 = slot2.plusHours(1);
        LocalDateTime slot4 = slot1.plusDays(1);
        Session session1 = new Session(1, "room1", singletonList(speaker1), "title1", "desc1", slot1, slot1.plusHours(1));
        Session session2 = new Session(2, "room2", singletonList(speaker2), "title2", "desc2", slot1, slot1.plusHours(1));
        Session session3 = new Session(3, "room1", singletonList(speaker3), "title3", "desc3", slot2, slot2.plusHours(1));
        Session session4 = new Session(4, "room1", singletonList(speaker4), "title4", "desc4", slot3, slot3.plusHours(1));
        Session session5 = new Session(5, "room1", singletonList(speaker5), "title5", "desc5", slot4, slot4.plusHours(1));

        // When
        Schedule result = appMapper.toSchedule(Arrays.asList(session1, session2, session3, session4, session5));

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDay()).isEqualTo(slot1.toLocalDate());
        assertThat(result.get(0).getSlots()).hasSize(3);
        assertThat(result.get(0).getSlots().get(0).getSessions()).containsAllOf(session1, session2);
        assertThat(result.get(0).getSlots().get(0).getTime()).isEqualTo(slot1);
        assertThat(result.get(0).getSlots().get(1).getSessions()).containsExactly(session3);
        assertThat(result.get(1).getSlots()).hasSize(1);
        assertThat(result.get(1).getSlots().get(0).getSessions()).containsExactly(session5);
    }
}
