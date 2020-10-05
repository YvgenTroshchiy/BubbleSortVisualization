package com.troshchiy.bubblesortvisualization.util;

import static com.troshchiy.bubblesortvisualization.App.APP;

public class UiUtils {

    /**
     * Avoid creating an instance of this class
     */
    private UiUtils() {
        throw new AssertionError();
    }

    public static int convertDpToPx(float dp) {
        return (int) (dp * APP.getResources().getDisplayMetrics().density + 0.5f);
    }

}