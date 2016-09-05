package com.nilhcem.droidconat.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

public class Animations {

    private Animations() {
        throw new UnsupportedOperationException();
    }

    public static void scale(View view, float ratioFrom, float ratioTo, long durationMillis) {
        ScaleAnimation anim = new ScaleAnimation(ratioFrom, ratioTo, ratioFrom, ratioTo, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillBefore(true);
        anim.setFillAfter(true);
        anim.setFillEnabled(true);
        anim.setDuration(durationMillis);
        anim.setInterpolator(new OvershootInterpolator());
        view.startAnimation(anim);
    }
}
