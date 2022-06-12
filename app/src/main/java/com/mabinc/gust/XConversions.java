package com.mabinc.gust;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.transition.TransitionManager;

public class XConversions {

    Context context;

    public XConversions(Context context) {
        this.context = context;
    }

    public void swipeViews(ConstraintLayout constLayout, ConstraintSet constraintSet) {
        TransitionManager.beginDelayedTransition(constLayout);
        constraintSet.applyTo(constLayout);
    }
}
