package com.nilhcem.droidconat.ui.venue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.nilhcem.droidconat.R;
import com.nilhcem.droidconat.ui.BaseActivity;
import com.nilhcem.droidconat.ui.BaseActivityPresenter;

import se.emilsjolander.intentbuilder.IntentBuilder;
import uk.co.senab.photoview.PhotoView;

@IntentBuilder
public class ZoomableImageActivity extends BaseActivity<ZoomableImageActivity.ZoomableImageActivityPresenter> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZoomableImageActivityIntentBuilder.inject(getIntent(), this);
        PhotoView view = new PhotoView(this);

        // No drawable-nodpi venue_rooms file
        // view.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.venue_rooms));
        view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setTitle(R.string.venue_see_rooms);
        setContentView(view);
    }

    @Override
    protected ZoomableImageActivityPresenter newPresenter() {
        return new ZoomableImageActivityPresenter(this);
    }

    static class ZoomableImageActivityPresenter extends BaseActivityPresenter<ZoomableImageActivity> {
        public ZoomableImageActivityPresenter(ZoomableImageActivity view) {
            super(view);
        }
    }
}

