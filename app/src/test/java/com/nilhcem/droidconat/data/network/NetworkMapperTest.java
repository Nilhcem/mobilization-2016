package com.nilhcem.droidconat.data.network;

import android.os.Build;

import com.nilhcem.droidconat.BuildConfig;
import com.nilhcem.droidconat.data.app.AppMapper;
import com.nilhcem.droidconat.data.app.model.Room;
import com.nilhcem.droidconat.data.network.model.Session;
import com.nilhcem.droidconat.data.network.model.Speaker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.threeten.bp.LocalDateTime;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Collections.singletonList;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class NetworkMapperTest {

    private final AppMapper appMapper = new AppMapper();
    private final NetworkMapper networkMapper = new NetworkMapper(appMapper);

    @Test
    public void should_convert_network_speakers_to_app_speakers() {
        // Given
        Speaker speaker1 = new Speaker(1, "speaker1", "title1", "bio1", "website1", "twitter1", "github1", "photo1");
        Speaker speaker2 = new Speaker(2, "speaker2", "title2", "bio2", "website2", "twitter2", "github2", "photo2");
        List<Speaker> speakers = Arrays.asList(speaker1, speaker2);

        // When
        List<com.nilhcem.droidconat.data.app.model.Speaker> result = networkMapper.toAppSpeakers(speakers);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo("speaker1");
        assertThat(result.get(0).getTitle()).isEqualTo("title1");
        assertThat(result.get(0).getBio()).isEqualTo("bio1");
        assertThat(result.get(0).getWebsite()).isEqualTo("website1");
        assertThat(result.get(0).getTwitter()).isEqualTo("twitter1");
        assertThat(result.get(0).getGithub()).isEqualTo("github1");
        assertThat(result.get(0).getPhoto()).isEqualTo("photo1");
        assertThat(result.get(1).getId()).isEqualTo(2);
        assertThat(result.get(1).getName()).isEqualTo("speaker2");
        assertThat(result.get(1).getTitle()).isEqualTo("title2");
        assertThat(result.get(1).getBio()).isEqualTo("bio2");
        assertThat(result.get(1).getWebsite()).isEqualTo("website2");
        assertThat(result.get(1).getTwitter()).isEqualTo("twitter2");
        assertThat(result.get(1).getGithub()).isEqualTo("github2");
        assertThat(result.get(1).getPhoto()).isEqualTo("photo2");
    }

    @Test
    public void should_convert_network_sessions_to_app_sessions() {
        // Given
        LocalDateTime startAt = LocalDateTime.now();
        Session session = new Session(1, startAt, 20, Room.NONE.id, singletonList(10), "title", "description");
        Map<Integer, com.nilhcem.droidconat.data.app.model.Speaker> speakersMap = new HashMap<>();
        speakersMap.put(10, new com.nilhcem.droidconat.data.app.model.Speaker(10, "ten", null, null, null, null, null, null));

        // When
        List<com.nilhcem.droidconat.data.app.model.Session> result = networkMapper.toAppSessions(singletonList(session), speakersMap);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getFromTime()).isEqualTo(startAt);
        assertThat(result.get(0).getToTime()).isEqualTo(startAt.plusMinutes(20));
        assertThat(result.get(0).getRoom()).isEqualTo(Room.NONE.label);
        assertThat(result.get(0).getSpeakers().get(0).getId()).isEqualTo(10);
        assertThat(result.get(0).getSpeakers().get(0).getName()).isEqualTo("ten");
        assertThat(result.get(0).getTitle()).isEqualTo("title");
        assertThat(result.get(0).getDescription()).isEqualTo("description");
    }
}
