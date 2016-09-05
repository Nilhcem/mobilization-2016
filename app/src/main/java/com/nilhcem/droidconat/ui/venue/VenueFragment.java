package com.nilhcem.droidconat.ui.venue;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.nilhcem.droidconat.R;
import com.nilhcem.droidconat.ui.BaseFragment;
import com.nilhcem.droidconat.ui.BaseFragmentPresenter;
import com.nilhcem.droidconat.utils.Intents;
import com.nilhcem.droidconat.utils.Views;

import butterknife.BindView;
import butterknife.OnClick;

public class VenueFragment extends BaseFragment {

    @BindView(R.id.venue_image) ImageView photo;

    private static final float PHOTO_RATIO = 0.406f;
    private static final String COORDINATES_URI = "geo:48.2392867,16.3751354?q=" + Uri.encode("Technikum Wien | University of Applied Sciences");

    @Override
    protected BaseFragmentPresenter newPresenter() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.venue, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPhotoSize();
    }

    @OnClick(R.id.venue_rooms)
    void openRoomsPlan() {
        startActivity(new Intent(getContext(), ZoomableImageActivity.class));
    }

    @OnClick(R.id.venue_locate)
    void openMapsLocation() {
        if (!Intents.startUri(getContext(), COORDINATES_URI)) {
            View view = getView();
            if (view != null) {
                Snackbar.make(view, R.string.venue_see_location_error, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void initPhotoSize() {
        photo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = photo.getWidth();
                if (width != 0) {
                    Views.removeOnGlobalLayoutListener(photo.getViewTreeObserver(), this);
                    photo.getLayoutParams().height = Math.round(width * PHOTO_RATIO);
                    photo.requestLayout();
                }
            }
        });
    }
}
