package com.levelup.palabre.inoreaderforpalabre.ui.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.levelup.palabre.inoreaderforpalabre.R;

public class TintedProgressBar extends ProgressBar {
    public TintedProgressBar(Context context) {
        super(context);
        init();
    }

    public TintedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TintedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        if (getIndeterminateDrawable() != null) {
            getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }
        if (getProgressDrawable() != null) {
            getProgressDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }
    }
}