package com.nilhcem.mobilization.ui.sessions.list;

import com.nilhcem.mobilization.data.app.model.Session;

import java.util.List;

public interface SessionsListMvp {

    interface View {
        void initToobar(String title);

        void initSessionsList(List<Session> sessions);

        void startSessionDetails(Session session);
    }
}
