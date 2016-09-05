package com.nilhcem.droidconat.ui.schedule.day;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.nilhcem.droidconat.DroidconApp;
import com.nilhcem.droidconat.R;
import com.nilhcem.droidconat.data.app.SelectedSessionsMemory;
import com.nilhcem.droidconat.data.app.model.ScheduleDay;
import com.nilhcem.droidconat.data.app.model.ScheduleSlot;
import com.nilhcem.droidconat.data.app.model.Session;
import com.nilhcem.droidconat.ui.BaseFragment;
import com.nilhcem.droidconat.ui.sessions.details.SessionDetailsActivityIntentBuilder;
import com.nilhcem.droidconat.ui.sessions.list.SessionsListActivityIntentBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

@FragmentWithArgs
public class ScheduleDayFragment extends BaseFragment<ScheduleDayPresenter> implements ScheduleDayMvp.View, ScheduleDayEntry.OnSessionClickListener {

    @Arg boolean allSessions;
    @Arg ScheduleDay scheduleDay;

    @Inject Picasso picasso;
    @Inject SelectedSessionsMemory selectedSessionsMemory;

    @BindView(R.id.schedule_day_recyclerview) RecyclerView recyclerView;

    private RecyclerView.Adapter<ScheduleDayEntry> adapter;

    @Override
    protected ScheduleDayPresenter newPresenter() {
        return new ScheduleDayPresenter(this, scheduleDay);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        DroidconApp.get(getContext()).component().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule_day, container, false);
    }

    @Override
    public void initSlotsList(List<ScheduleSlot> slots) {
        if (allSessions) {
            adapter = new ScheduleDayFragmentAdapterAllSessions(slots, picasso, selectedSessionsMemory, this);
        } else {
            adapter = new ScheduleDayFragmentAdapterMySessions(slots, selectedSessionsMemory, picasso, this);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void refreshSlotsList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFreeSlotClicked(ScheduleSlot slot) {
        startActivity(new SessionsListActivityIntentBuilder(slot).build(getContext()));
    }

    @Override
    public void onSelectedSessionClicked(Session session) {
        startActivity(new SessionDetailsActivityIntentBuilder(session).build(getContext()));
    }
}
