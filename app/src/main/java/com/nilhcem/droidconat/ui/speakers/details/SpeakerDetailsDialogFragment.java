package com.nilhcem.droidconat.ui.speakers.details;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nilhcem.droidconat.DroidconApp;
import com.nilhcem.droidconat.R;
import com.nilhcem.droidconat.data.app.model.Speaker;
import com.nilhcem.droidconat.ui.core.picasso.CircleTransformation;
import com.nilhcem.droidconat.utils.Intents;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SpeakerDetailsDialogFragment extends AppCompatDialogFragment {

    @Inject Picasso picasso;

    @BindView(R.id.speaker_details_name) TextView name;
    @BindView(R.id.speaker_details_title) TextView title;
    @BindView(R.id.speaker_details_bio) TextView bio;
    @BindView(R.id.speaker_details_photo) ImageView photo;
    @BindView(R.id.speaker_details_links_container) ViewGroup linksContainer;
    @BindView(R.id.speaker_details_twitter) ImageView twitter;
    @BindView(R.id.speaker_detail_github) ImageView github;
    @BindView(R.id.speaker_details_website) ImageView website;

    private Unbinder unbinder;

    private static final String EXTRA_SPEAKER = "speaker";

    public static void show(@NonNull Speaker speaker, @NonNull FragmentManager fm) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_SPEAKER, speaker);
        SpeakerDetailsDialogFragment fragment = new SpeakerDetailsDialogFragment();
        fragment.setArguments(args);
        fragment.show(fm, SpeakerDetailsDialogFragment.class.getSimpleName());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DroidconApp.get(getContext()).component().inject(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getContext()).inflate(R.layout.speaker_details, null);
        unbinder = ButterKnife.bind(this, view);
        bindSpeaker(getArguments().getParcelable(EXTRA_SPEAKER));
        return new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton(R.string.speaker_details_hide, (dialog, which) -> dismiss())
                .create();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    private void bindSpeaker(Speaker speaker) {
        name.setText(speaker.getName());
        title.setText(speaker.getTitle());
        bio.setText(speaker.getBio());

        String photoUrl = speaker.getPhoto();
        if (!TextUtils.isEmpty(photoUrl)) {
            picasso.load(photoUrl).transform(new CircleTransformation()).into(photo);
        }

        bindLinks(speaker);
    }

    private void bindLinks(Speaker speaker) {
        boolean hasLink = false;

        if (!TextUtils.isEmpty(speaker.getTwitter())) {
            twitter.setVisibility(View.VISIBLE);
            twitter.setOnClickListener(v -> openLink("https://www.twitter.com/#!/" + speaker.getTwitter()));
            hasLink = true;
        }

        if (!TextUtils.isEmpty(speaker.getGithub())) {
            github.setVisibility(View.VISIBLE);
            github.setOnClickListener(v -> openLink("https://www.github.com/" + speaker.getGithub()));
            hasLink = true;
        }

        if (!TextUtils.isEmpty(speaker.getWebsite())) {
            website.setVisibility(View.VISIBLE);
            website.setOnClickListener(v -> openLink(speaker.getWebsite()));
            hasLink = true;
        }

        linksContainer.setVisibility(hasLink ? View.VISIBLE : View.GONE);
    }

    private void openLink(String url) {
        Intents.startExternalUrl(getActivity(), url);
    }
}
