package com.nilhcem.droidconat.ui.sessions.list;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.nilhcem.droidconat.data.app.SelectedSessionsMemory;
import com.nilhcem.droidconat.data.app.model.Session;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SessionsListAdapter extends RecyclerView.Adapter<SessionsListEntry> {

    private final List<Session> sessions;
    private final Picasso picasso;
    private final SelectedSessionsMemory selectedSessionsMemory;
    private final SessionsListMvp.View listener;

    public SessionsListAdapter(List<Session> sessions, Picasso picasso, SelectedSessionsMemory selectedSessionsMemory, SessionsListMvp.View listener) {
        this.sessions = sessions;
        this.picasso = picasso;
        this.selectedSessionsMemory = selectedSessionsMemory;
        this.listener = listener;
    }

    @Override
    public SessionsListEntry onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SessionsListEntry(parent, picasso);
    }

    @Override
    public void onBindViewHolder(SessionsListEntry holder, int position) {
        Session session = sessions.get(position);
        holder.bindSession(session, selectedSessionsMemory.isSelected(session));
        holder.itemView.setOnClickListener(v -> listener.startSessionDetails(session));
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }
}
