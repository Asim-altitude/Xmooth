package com.smilieideas.searchwithinstagram.utils;

import android.view.View;

public class ImageUtils {

    private static float mItemScaleFactor = 1.2f;
    private static int mItemScaleDuration = 500;
    public static void zoomIn(View view) {
        if (view != null) {
            view.animate().scaleX(mItemScaleFactor).scaleY(mItemScaleFactor).setDuration(mItemScaleDuration).start();
        }
    }

    // 缩小
    public static void zoomOut(View view) {
        if (view != null) {
            view.animate().scaleX(1).scaleY(1).setDuration(mItemScaleDuration).start();
        }
    }

}
