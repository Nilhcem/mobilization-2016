package com.nilhcem.mobilization.ui.schedule.day;

import android.os.Build;

import com.nilhcem.mobilization.BuildConfig;
import com.nilhcem.mobilization.data.app.model.ScheduleDay;
import com.nilhcem.mobilization.data.app.model.ScheduleSlot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class ScheduleDayPresenterTest {

    @Mock ScheduleDayMvp.View view;

    private ScheduleDayPresenter presenter;
    private final List<ScheduleSlot> slots = new ArrayList<>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScheduleDay scheduleDay = new ScheduleDay(LocalDate.now(), slots);
        presenter = new ScheduleDayPresenter(view, scheduleDay);
    }

    @Test
    public void should_init_view_slots_when_view_is_created() {
        // When
        presenter.onViewCreated(null, null);

        // Then
        verify(view).initSlotsList(slots);
    }

    @Test
    public void should_refresh_slots_when_view_is_resuming() {
        // When
        presenter.onResume();

        // Then
        verify(view).refreshSlotsList();
    }
}
