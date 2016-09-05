package com.nilhcem.droidconat.data.database;

import android.os.Build;

import com.nilhcem.droidconat.BuildConfig;
import com.nilhcem.droidconat.core.moshi.LocalDateTimeAdapter;
import com.nilhcem.droidconat.data.app.AppMapper;
import com.nilhcem.droidconat.data.app.model.Room;
import com.nilhcem.droidconat.data.app.model.Session;
import com.nilhcem.droidconat.data.app.model.Speaker;
import com.squareup.moshi.Moshi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.threeten.bp.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class DbMapperTest {

    private DbMapper dbMapper;
    private final Moshi moshi = new Moshi.Builder().build();
    private final AppMapper appMapper = new AppMapper();
    private final LocalDateTime now = LocalDateTime.now();

    @Mock LocalDateTimeAdapter adapter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dbMapper = new DbMapper(moshi, appMapper, adapter);
    }

    @Test
    public void should_convert_db_sessions_to_app_sessions() {
        // Given
        when(adapter.fromText("now")).thenReturn(now);
        com.nilhcem.droidconat.data.database.model.Session session = new com.nilhcem.droidconat.data.database.model.Session(2, "now", 10, 3, "[1]", "title", "description");
        List<com.nilhcem.droidconat.data.database.model.Session> sessions = singletonList(session);
        Map<Integer, Speaker> speakersMap = new HashMap<>();
        Speaker speaker = new Speaker(1, "name", null, null, null, null, null, null);
        speakersMap.put(1, speaker);

        // When
        List<Session> result = dbMapper.toAppSessions(sessions, speakersMap);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(2);
        assertThat(result.get(0).getFromTime()).isEqualTo(now);
        assertThat(result.get(0).getTitle()).isEqualTo("title");
        assertThat(result.get(0).getDescription()).isEqualTo("description");
        assertThat(result.get(0).getSpeakers().get(0)).isEqualTo(speaker);
    }

    @Test
    public void should_convert_app_session_to_db_session() {
        // Given
        List<Speaker> speakers = singletonList(new Speaker(7, null, null, null, null, null, null, null));
        Session session = new Session(11, Room.NONE.label, speakers, "title", "description", now, now.plusMinutes(45));

        // When
        com.nilhcem.droidconat.data.database.model.Session result = dbMapper.fromAppSession(session);

        // Then
        assertThat(result.id).isEqualTo(11);
        assertThat(result.roomId).isEqualTo(Room.NONE.id);
        assertThat(result.speakersIds).isEqualTo("[7]");
        assertThat(result.title).isEqualTo("title");
        assertThat(result.description).isEqualTo("description");
    }

    @Test
    public void should_convert_app_speaker_to_db_speaker() {
        // Given
        Speaker speaker = new Speaker(10, "name", "title", "bio", "website", "twitter", "github", "photo");

        // When
        com.nilhcem.droidconat.data.database.model.Speaker result = dbMapper.fromAppSpeaker(speaker);

        // Then
        assertThat(result.id).isEqualTo(10);
        assertThat(result.name).isEqualTo("name");
        assertThat(result.title).isEqualTo("title");
        assertThat(result.bio).isEqualTo("bio");
        assertThat(result.website).isEqualTo("website");
        assertThat(result.twitter).isEqualTo("twitter");
        assertThat(result.github).isEqualTo("github");
        assertThat(result.photo).isEqualTo("photo");
    }

    @Test
    public void should_convert_db_speakers_to_app_speakers() {
        // Given
        com.nilhcem.droidconat.data.database.model.Speaker speaker = new com.nilhcem.droidconat.data.database.model.Speaker(58, "nilh", "dev", "bio", "nilhcem.com", "Nilhcem", "nilhcem", "photo");

        // When
        List<Speaker> result = dbMapper.toAppSpeakers(singletonList(speaker));

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(58);
        assertThat(result.get(0).getName()).isEqualTo("nilh");
        assertThat(result.get(0).getTitle()).isEqualTo("dev");
        assertThat(result.get(0).getBio()).isEqualTo("bio");
        assertThat(result.get(0).getWebsite()).isEqualTo("nilhcem.com");
        assertThat(result.get(0).getTwitter()).isEqualTo("Nilhcem");
        assertThat(result.get(0).getGithub()).isEqualTo("nilhcem");
        assertThat(result.get(0).getPhoto()).isEqualTo("photo");
    }
}
