package com.nilhcem.droidconat.ui.schedule.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nilhcem.droidconat.R;
import com.nilhcem.droidconat.data.app.model.Schedule;
import com.nilhcem.droidconat.ui.schedule.day.ScheduleDayFragmentBuilder;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

public class SchedulePagerAdapter extends FragmentPagerAdapter {

    private final Schedule schedule;
    private final String dayPattern;
    private final boolean allSessions;

    public SchedulePagerAdapter(Context context, FragmentManager fm, Schedule schedule, boolean allSessions) {
        super(fm);
        this.schedule = schedule;
        this.allSessions = allSessions;
        dayPattern = context.getString(R.string.schedule_pager_day_pattern);
    }

    @Override
    public Fragment getItem(int position) {
        return new ScheduleDayFragmentBuilder(allSessions, schedule.get(position)).build();
    }

    @Override
    public int getCount() {
        return schedule.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dayPattern, Locale.getDefault());
        return schedule.get(position).getDay().format(formatter);
    }
}
