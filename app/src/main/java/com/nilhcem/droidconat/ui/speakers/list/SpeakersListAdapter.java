package com.nilhcem.droidconat.ui.speakers.list;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.nilhcem.droidconat.data.app.model.Speaker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SpeakersListAdapter extends RecyclerView.Adapter<SpeakersListEntry> {

    private final List<Speaker> speakers = new ArrayList<>();
    private final Picasso picasso;
    private final SpeakersListMvp.View view;

    public SpeakersListAdapter(Picasso picasso, SpeakersListMvp.View view) {
        this.picasso = picasso;
        this.view = view;
    }

    @Override
    public SpeakersListEntry onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SpeakersListEntry(parent, picasso);
    }

    @Override
    public void onBindViewHolder(SpeakersListEntry holder, int position) {
        holder.bindSpeaker(speakers.get(position));
        holder.itemView.setOnClickListener(v -> view.showSpeakerDetails(speakers.get(position)));
    }

    @Override
    public int getItemCount() {
        return speakers.size();
    }

    public void setSpeakers(List<Speaker> speakers) {
        this.speakers.clear();
        this.speakers.addAll(speakers);
        notifyDataSetChanged();
    }
}
