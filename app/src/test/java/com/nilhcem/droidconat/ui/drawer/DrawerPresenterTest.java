package com.nilhcem.droidconat.ui.drawer;

import android.os.Build;

import com.nilhcem.droidconat.BuildConfig;
import com.nilhcem.droidconat.R;
import com.nilhcem.droidconat.ui.schedule.pager.SchedulePagerFragment;
import com.nilhcem.droidconat.ui.speakers.list.SpeakersListFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class DrawerPresenterTest {

    @Mock DrawerMvp.View view;
    private DrawerPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new DrawerPresenter(view);
    }

    @Test
    public void should_automatically_select_my_schedule_when_starting_the_app() {
        // When
        presenter.onPostCreate(null);

        // Then
        verify(view).showFragment(any(SchedulePagerFragment.class));
    }

    @Test
    public void should_do_nothing_when_selecting_a_category_which_is_already_selected() {
        // Given
        presenter.selectedItemId = R.id.drawer_nav_speakers;

        // When
        presenter.onNavigationItemSelected(R.id.drawer_nav_speakers);

        // Then
        verify(view, never()).showFragment(any());
    }

    @Test
    public void should_show_speakers_fragment_when_selecting_speakers_from_the_navigation() {
        // Given
        presenter.selectedItemId = 0;

        // When
        presenter.onNavigationItemSelected(R.id.drawer_nav_speakers);

        // Then
        verify(view).showFragment(any(SpeakersListFragment.class));
    }

    @Test
    public void should_close_navigation_drawer_when_pressing_back_if_already_opened() {
        // Given
        when(view.isNavigationDrawerOpen()).thenReturn(true);

        // When
        boolean result = presenter.onBackPressed();

        // Then
        assertThat(result).isTrue();
        verify(view).closeNavigationDrawer();
    }

    @Test
    public void should_go_back_to_agenda_when_pressing_back_while_navigation_drawer_is_already_closed_and_category_is_not_schedule() {
        // Given
        presenter.toolbarTitle = R.string.drawer_nav_speakers;
        when(view.isNavigationDrawerOpen()).thenReturn(false);

        // When
        boolean result = presenter.onBackPressed();

        // Then
        assertThat(result).isTrue();
        verify(view).selectDrawerMenuItem(R.id.drawer_nav_schedule);
        verify(view, times(1)).closeNavigationDrawer();
    }

    @Test
    public void should_go_back_when_pressing_back_while_navigation_drawer_is_already_closed_and_category_is_schedule() {
        // Given
        presenter.toolbarTitle = R.string.drawer_nav_schedule;
        when(view.isNavigationDrawerOpen()).thenReturn(false);

        // When
        boolean result = presenter.onBackPressed();

        // Then
        assertThat(result).isFalse();
        verify(view, never()).selectDrawerMenuItem(R.id.drawer_nav_schedule);
        verify(view, never()).closeNavigationDrawer();
    }

    @Test
    public void should_restore_toolbar_title_when_state_changes() {
        // Given
        presenter.toolbarTitle = R.string.drawer_nav_speakers;

        // When
        presenter.onRestoreInstanceState(null);

        // Then
        verify(view).updateToolbarTitle(R.string.drawer_nav_speakers);
    }
}
