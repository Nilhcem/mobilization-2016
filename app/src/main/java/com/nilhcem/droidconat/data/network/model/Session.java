package com.nilhcem.droidconat.data.network.model;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import lombok.Value;

@Value
public class Session {

    int id;
    LocalDateTime startAt;
    int duration;
    int roomId;
    List<Integer> speakersId;
    String title;
    String description;
}
