package com.nilhcem.droidconat.ui.schedule.day;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nilhcem.droidconat.R;
import com.nilhcem.droidconat.data.app.model.Speaker;
import com.nilhcem.droidconat.ui.core.picasso.CircleTransformation;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleDayEntrySpeaker extends LinearLayout {

    @BindView(R.id.schedule_day_entry_speaker_photo) ImageView photo;
    @BindView(R.id.schedule_day_entry_speaker_name) TextView name;

    public ScheduleDayEntrySpeaker(Context context, Speaker speaker, Picasso picasso) {
        super(context);
        setOrientation(HORIZONTAL);
        int padding = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics()));
        setPadding(0, padding, 0, padding);

        LayoutInflater.from(context).inflate(R.layout.schedule_day_entry_speaker, this);
        ButterKnife.bind(this, this);
        bind(speaker, picasso);
    }

    private void bind(Speaker speaker, Picasso picasso) {
        String photoUrl = speaker.getPhoto();
        if (!TextUtils.isEmpty(photoUrl)) {
            picasso.load(photoUrl).transform(new CircleTransformation()).into(photo);
        }

        name.setText(speaker.getName());
    }
}
