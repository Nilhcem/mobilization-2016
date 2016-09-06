package com.nilhcem.mobilization.ui.sessions.list;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nilhcem.mobilization.R;
import com.nilhcem.mobilization.data.app.model.Session;
import com.nilhcem.mobilization.ui.core.picasso.CircleTransformation;
import com.nilhcem.mobilization.ui.core.recyclerview.BaseViewHolder;
import com.nilhcem.mobilization.utils.App;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class SessionsListEntry extends BaseViewHolder {

    @BindView(R.id.sessions_list_entry_photo) ImageView photo;
    @BindView(R.id.sessions_list_entry_title) TextView title;
    @BindView(R.id.sessions_list_entry_selected_state) ImageView selectedState;
    @BindView(R.id.sessions_list_entry_room) TextView room;
    @BindView(R.id.sessions_list_entry_description) TextView description;

    private final Picasso picasso;

    public SessionsListEntry(ViewGroup parent, Picasso picasso) {
        super(parent, R.layout.sessions_list_entry);
        this.picasso = picasso;
    }

    public void bindSession(Session session, boolean isSelected) {
        String photoUrl = App.getPhotoUrl(session);
        if (TextUtils.isEmpty(photoUrl)) {
            photo.setImageDrawable(null);
        } else {
            picasso.load(photoUrl).transform(new CircleTransformation()).into(photo);
        }

        title.setText(session.getTitle());
        room.setText(session.getRoom());
        description.setText(session.getDescription());

        int selectedRes = isSelected ? R.drawable.sessions_list_entry_selected : R.drawable.sessions_list_entry_default;
        selectedState.setImageDrawable(ContextCompat.getDrawable(selectedState.getContext(), selectedRes));
    }
}
