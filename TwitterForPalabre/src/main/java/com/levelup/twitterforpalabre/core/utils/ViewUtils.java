package com.levelup.twitterforpalabre.core.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class ViewUtils {

    /**
     * Converts dip to pixel.
     *
     * @param dip that should be converted.
     * @return the converted rounded pixel.
     */
    public static int dipToPixel(Context context, int dip) {
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        return (int) ((dip * displayMetrics.density) + 0.5);
    }

}
