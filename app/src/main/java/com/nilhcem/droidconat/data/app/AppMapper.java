package com.nilhcem.droidconat.data.app;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nilhcem.droidconat.data.app.model.Room;
import com.nilhcem.droidconat.data.app.model.Schedule;
import com.nilhcem.droidconat.data.app.model.ScheduleDay;
import com.nilhcem.droidconat.data.app.model.ScheduleSlot;
import com.nilhcem.droidconat.data.app.model.Session;
import com.nilhcem.droidconat.data.app.model.Speaker;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import java8.util.stream.Collectors;

import static java8.util.stream.StreamSupport.stream;

public class AppMapper {

    @Inject
    public AppMapper() {
    }

    public Map<Integer, Speaker> speakersToMap(@NonNull List<Speaker> from) {
        return stream(from).collect(Collectors.toMap(Speaker::getId, speaker -> speaker));
    }

    public List<Speaker> toSpeakersList(@Nullable List<Integer> speakerIds, @NonNull Map<Integer, Speaker> speakersMap) {
        if (speakerIds == null) {
            return null;
        }
        return stream(speakerIds).map(speakersMap::get).collect(Collectors.toList());
    }

    public List<Integer> toSpeakersIds(@Nullable List<Speaker> speakers) {
        if (speakers == null) {
            return null;
        }
        return stream(speakers).map(Speaker::getId).collect(Collectors.toList());
    }

    public Schedule toSchedule(@NonNull List<Session> sessions) {
        // Map and sort Session per start date
        Collections.sort(sessions, (lhs, rhs) -> lhs.getFromTime().compareTo(rhs.getFromTime()));

        // Gather Sessions per ScheduleSlot
        List<ScheduleSlot> scheduleSlots = mapToScheduleSlots(sessions);

        // Gather ScheduleSlots per ScheduleDays
        return mapToScheduleDays(scheduleSlots);
    }

    private List<ScheduleSlot> mapToScheduleSlots(@NonNull List<Session> sortedSessions) {
        List<ScheduleSlot> slots = new ArrayList<>();

        LocalDateTime previousTime = null;
        List<Session> previousSessionsList = null;

        LocalDateTime currentTime;
        for (Session currentSession : sortedSessions) {
            currentTime = currentSession.getFromTime();
            if (previousSessionsList != null) {
                if (currentTime.equals(previousTime)) {
                    previousSessionsList.add(currentSession);
                } else {
                    slots.add(new ScheduleSlot(previousTime, sortPerRoomId(previousSessionsList)));
                    previousSessionsList = null;
                }
            }

            if (previousSessionsList == null) {
                previousTime = currentTime;
                previousSessionsList = new ArrayList<>();
                previousSessionsList.add(currentSession);
            }
        }

        if (previousSessionsList != null) {
            slots.add(new ScheduleSlot(previousTime, sortPerRoomId(previousSessionsList)));
        }
        return slots;
    }

    private Schedule mapToScheduleDays(@NonNull List<ScheduleSlot> scheduleSlots) {
        Schedule schedule = new Schedule();

        LocalDate previousDay = null;
        List<ScheduleSlot> previousSlotList = null;

        LocalDate currentDay;
        for (ScheduleSlot currentSlot : scheduleSlots) {
            currentDay = LocalDate.from(currentSlot.getTime());
            if (previousSlotList != null) {
                if (currentDay.equals(previousDay)) {
                    previousSlotList.add(currentSlot);
                } else {
                    schedule.add(new ScheduleDay(previousDay, previousSlotList));
                    previousSlotList = null;
                }
            }

            if (previousSlotList == null) {
                previousDay = currentDay;
                previousSlotList = new ArrayList<>();
                previousSlotList.add(currentSlot);
            }
        }

        if (previousSlotList != null) {
            schedule.add(new ScheduleDay(previousDay, previousSlotList));
        }
        return schedule;
    }

    private List<Session> sortPerRoomId(@NonNull List<Session> list) {
        Collections.sort(list, (lhs, rhs) -> Room.getFromLabel(lhs.getRoom()).id - Room.getFromLabel(rhs.getRoom()).id);
        return list;
    }
}
