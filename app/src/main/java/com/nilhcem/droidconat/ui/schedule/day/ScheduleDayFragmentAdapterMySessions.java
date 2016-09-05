package com.nilhcem.droidconat.ui.schedule.day;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.nilhcem.droidconat.data.app.SelectedSessionsMemory;
import com.nilhcem.droidconat.data.app.model.Room;
import com.nilhcem.droidconat.data.app.model.ScheduleSlot;
import com.nilhcem.droidconat.data.app.model.Session;
import com.squareup.picasso.Picasso;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import static java8.util.stream.StreamSupport.stream;

public class ScheduleDayFragmentAdapterMySessions extends RecyclerView.Adapter<ScheduleDayEntry> {

    private final List<ScheduleSlot> slots;
    private final SelectedSessionsMemory selectedSessionsMemory;
    private final Picasso picasso;
    private final ScheduleDayEntry.OnSessionClickListener listener;

    public ScheduleDayFragmentAdapterMySessions(List<ScheduleSlot> slots, SelectedSessionsMemory selectedSessionsMemory, Picasso picasso, ScheduleDayEntry.OnSessionClickListener listener) {
        this.slots = slots;
        this.selectedSessionsMemory = selectedSessionsMemory;
        this.picasso = picasso;
        this.listener = listener;
    }

    @Override
    public ScheduleDayEntry onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleDayEntry(parent, picasso, listener);
    }

    @Override
    public void onBindViewHolder(ScheduleDayEntry holder, int position) {
        ScheduleSlot slot = slots.get(position);
        List<Session> slotSessions = slot.getSessions();
        Session selectedSession = findSelectedSession(slot.getTime(), slotSessions);

        if (selectedSession == null) {
            if (slotSessions.size() > 1) {
                holder.bindFreeSlot(slot);
            } else {
                Session session = slotSessions.get(0);
                if (session.getRoom().equals(Room.NONE.label)) {
                    holder.bindBreakSlot(slot, session, true);
                } else {
                    holder.bindSelectedSession(slot, session, true, selectedSessionsMemory.isSelected(session));
                }
            }
        } else {
            holder.bindSelectedSession(slot, selectedSession, true, selectedSessionsMemory.isSelected(selectedSession));
        }
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    private Session findSelectedSession(LocalDateTime slotTime, List<Session> slotSessions) {
        Integer selectedSessionId = selectedSessionsMemory.get(slotTime);
        if (selectedSessionId == null) {
            return null;
        }

        return stream(slotSessions)
                .filter(session -> session.getId() == selectedSessionId)
                .findFirst().orElse(null);
    }
}
