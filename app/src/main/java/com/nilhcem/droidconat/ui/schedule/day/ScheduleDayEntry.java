package com.nilhcem.droidconat.ui.schedule.day;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nilhcem.droidconat.R;
import com.nilhcem.droidconat.data.app.model.ScheduleSlot;
import com.nilhcem.droidconat.data.app.model.Session;
import com.nilhcem.droidconat.data.app.model.Speaker;
import com.nilhcem.droidconat.ui.core.recyclerview.BaseViewHolder;
import com.squareup.picasso.Picasso;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.List;

import butterknife.BindView;
import java8.util.stream.StreamSupport;

public class ScheduleDayEntry extends BaseViewHolder {

    public interface OnSessionClickListener {
        void onFreeSlotClicked(ScheduleSlot slot);

        void onSelectedSessionClicked(Session session);
    }

    @BindView(R.id.schedule_day_entry_time) TextView time;

    @BindView(R.id.schedule_day_entry_break_card) CardView breakCard;
    @BindView(R.id.schedule_day_entry_break_text) TextView breakText;
    @BindView(R.id.schedule_day_entry_break_time) TextView breakTime;

    @BindView(R.id.schedule_day_entry_browse_card) CardView browseCard;

    @BindView(R.id.schedule_day_entry_session_card) CardView sessionCard;
    @BindView(R.id.schedule_day_entry_session_title) TextView sessionTitle;
    @BindView(R.id.schedule_day_entry_session_time) TextView sessionTime;
    @BindView(R.id.schedule_day_entry_session_room) TextView sessionRoom;
    @BindView(R.id.schedule_day_entry_session_selected_state) ImageView sessionSelectedState;
    @BindView(R.id.schedule_day_entry_slot_speakers) ViewGroup sessionSpeakers;

    private final Picasso picasso;
    private final OnSessionClickListener listener;

    public ScheduleDayEntry(ViewGroup parent, Picasso picasso, OnSessionClickListener listener) {
        super(parent, R.layout.schedule_day_entry);
        this.picasso = picasso;
        this.listener = listener;
    }

    public void bindFreeSlot(ScheduleSlot slot) {
        breakCard.setVisibility(View.GONE);
        sessionCard.setVisibility(View.GONE);

        bindTime(slot, true);
        browseCard.setVisibility(View.VISIBLE);
        browseCard.setOnClickListener(v -> listener.onFreeSlotClicked(slot));
    }

    public void bindBreakSlot(ScheduleSlot slot, Session session, boolean showTime) {
        browseCard.setVisibility(View.GONE);
        sessionCard.setVisibility(View.GONE);

        bindTime(slot, showTime);
        breakCard.setVisibility(View.VISIBLE);
        breakText.setText(session.getTitle());
        breakTime.setText(formatSessionTime(session));
    }

    public void bindSelectedSession(ScheduleSlot slot, Session session, boolean showTime, boolean isSelected) {
        breakCard.setVisibility(View.GONE);
        browseCard.setVisibility(View.GONE);

        bindTime(slot, showTime);
        sessionCard.setVisibility(View.VISIBLE);
        sessionTitle.setText(session.getTitle());
        sessionTime.setText(formatSessionTime(session));
        bindRoom(session, sessionRoom);
        bindSessionSpeakers(session);

        int selectedRes = isSelected ? R.drawable.sessions_list_selected : R.drawable.sessions_list_default;
        sessionSelectedState.setImageDrawable(ContextCompat.getDrawable(sessionSelectedState.getContext(), selectedRes));

        sessionCard.setOnClickListener(v -> listener.onSelectedSessionClicked(session));
    }

    private String formatTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        return time.format(formatter);
    }

    private void bindTime(ScheduleSlot slot, boolean showTime) {
        if (showTime) {
            String timeStr = formatTime(slot.getTime());
            time.setVisibility(View.VISIBLE);
            time.setText(timeStr);
        } else {
            time.setVisibility(View.INVISIBLE);
        }
    }

    private String formatSessionTime(Session session) {
        LocalDateTime fromTime = session.getFromTime();
        LocalDateTime toTime = session.getToTime();
        long minutes = ChronoUnit.MINUTES.between(fromTime, toTime);
        return itemView.getContext().getString(R.string.schedule_day_entry_session_time_format,
                formatTime(fromTime), formatTime(toTime), minutes);
    }

    private void bindRoom(Session session, TextView view) {
        String room = session.getRoom();
        if (TextUtils.isEmpty(room)) {
            view.setVisibility(View.GONE);
        } else {
            view.setText(room);
            view.setVisibility(View.VISIBLE);
        }
    }

    private void bindSessionSpeakers(Session session) {
        sessionSpeakers.removeAllViews();

        List<Speaker> speakers = session.getSpeakers();
        if (speakers != null) {
            StreamSupport.stream(speakers)
                    .map(speaker -> new ScheduleDayEntrySpeaker(sessionSpeakers.getContext(), speaker, picasso))
                    .forEach(sessionSpeakers::addView);
        }
    }
}
