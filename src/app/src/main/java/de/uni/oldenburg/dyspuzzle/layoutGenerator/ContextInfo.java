package de.uni.oldenburg.dyspuzzle.layoutGenerator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;

public class ContextInfo {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private int mStatusbarHeight = 0;
    private int mNavigationBarHeight = 0;
    private int mActionbarHeight = 0;


    public ContextInfo(Context context){
        mContext = context;
        mStatusbarHeight = getStatusBarHeight();
        mNavigationBarHeight = getNavigationBarHeight();
        mActionbarHeight = getActionBarHeight();
    }

    public int getTotalScreenWidth() {

        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getTotalScreenHeight() {
                return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public int getScreenWidth(int orientation) {

        int adjust = orientation == Configuration.ORIENTATION_LANDSCAPE
                ? 0
                : 0;
        return Resources.getSystem().getDisplayMetrics().widthPixels - adjust;
    }

    public int getScreenHeight(int orientation) {

        int adjust = orientation == Configuration.ORIENTATION_LANDSCAPE
                ? mStatusbarHeight + mActionbarHeight
                : mStatusbarHeight + mActionbarHeight;

        return Resources.getSystem().getDisplayMetrics().heightPixels - adjust;
    }

    public static int getStatusBarHeight() {

        int resourceId = getResourceId("status_bar_height");
        return getDimensionPixelSize(resourceId);
    }

    public static int getNavigationBarHeight() {

        Resources resources = mContext.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return getDimensionPixelSize(resourceId);
    }

    public static int getActionBarHeight() {

        final TypedArray ta = mContext.getTheme().obtainStyledAttributes(
                new int[] {android.R.attr.actionBarSize});
        int actionBarHeight = (int) ta.getDimension(0, 0);
        return actionBarHeight;
    }

    private static int getResourceId(String resourceName) {

        int resourceId = mContext.getResources().getIdentifier(resourceName, "dimen", "android");
        return resourceId;
    }

    private static int getDimensionPixelSize(int resourceId) {

        if (resourceId > 0) {
            return mContext.getResources().getDimensionPixelSize(resourceId);
        }

        return 0;
    }
}
