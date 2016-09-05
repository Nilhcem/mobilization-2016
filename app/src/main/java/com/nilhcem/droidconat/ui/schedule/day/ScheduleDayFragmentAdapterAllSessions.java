package com.nilhcem.droidconat.ui.schedule.day;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.nilhcem.droidconat.data.app.SelectedSessionsMemory;
import com.nilhcem.droidconat.data.app.model.Room;
import com.nilhcem.droidconat.data.app.model.ScheduleSlot;
import com.nilhcem.droidconat.data.app.model.Session;
import com.squareup.picasso.Picasso;

import java.util.List;

import java8.util.stream.Collectors;

import static java8.util.stream.StreamSupport.stream;

public class ScheduleDayFragmentAdapterAllSessions extends RecyclerView.Adapter<ScheduleDayEntry> {

    private final List<Pair<Session, ScheduleSlot>> sessions;
    private final Picasso picasso;
    private final SelectedSessionsMemory selectedSessionsMemory;
    private final ScheduleDayEntry.OnSessionClickListener listener;

    public ScheduleDayFragmentAdapterAllSessions(List<ScheduleSlot> slots, Picasso picasso, SelectedSessionsMemory selectedSessionsMemory, ScheduleDayEntry.OnSessionClickListener listener) {
        sessions = toSessionsSlotsPair(slots);
        this.picasso = picasso;
        this.selectedSessionsMemory = selectedSessionsMemory;
        this.listener = listener;
    }

    @Override
    public ScheduleDayEntry onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleDayEntry(parent, picasso, listener);
    }

    @Override
    public void onBindViewHolder(ScheduleDayEntry holder, int position) {
        Pair<Session, ScheduleSlot> pair = sessions.get(position);
        Session session = pair.first;
        ScheduleSlot slot = pair.second;

        if (session.getRoom().equals(Room.NONE.label)) {
            holder.bindBreakSlot(slot, session, shouldShowTime(slot, position));
        } else {
            holder.bindSelectedSession(slot, session, shouldShowTime(slot, position), selectedSessionsMemory.isSelected(session));
        }
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    private List<Pair<Session, ScheduleSlot>> toSessionsSlotsPair(List<ScheduleSlot> slots) {
        return stream(slots).flatMap(scheduleSlot -> stream(scheduleSlot.getSessions())
                .map(session -> new Pair<>(session, scheduleSlot))
        ).collect(Collectors.toList());
    }

    private boolean shouldShowTime(ScheduleSlot slot, int position) {
        return position == 0 || !slot.getTime().equals(sessions.get(position - 1).second.getTime());
    }
}
