package com.bel.lampa;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;

public class Utils {
    public static int dpTopx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static boolean isEmptyString(String path) {
        boolean isEmpty;
        if (path == null) isEmpty = true;
        else isEmpty = path.trim().isEmpty();
        return isEmpty;
    }
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
