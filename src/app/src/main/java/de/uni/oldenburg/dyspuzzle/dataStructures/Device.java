package de.uni.oldenburg.dyspuzzle.dataStructures;

import android.graphics.Point;

// Singleton class to safe the information of the user
public class Device {

    private int statusbarHeight;
    private int navigationBarHeight;
    private int actionbarHeight;
    private int screenHeight;
    private int screenWidth;
    private Point mRootLayoutOrigin = new Point();

    private static Device device = null;

    // a private constructor so no instances can be made outside this class
    private Device() {}

    // Everytime an instance is needed, call this function
    // synchronized to make the call thread-safe
    public static synchronized Device getInstance() {

        if(device == null)
            device = new Device();

        return device;
    }

    public int getStatusbarHeight() {
        return statusbarHeight;
    }

    public void setStatusbarHeight(int statusbarHeight) {
        this.statusbarHeight = statusbarHeight;
    }

    public int getNavigationBarHeight() {
        return navigationBarHeight;
    }

    public void setNavigationBarHeight(int navigationBarHeight) {
        this.navigationBarHeight = navigationBarHeight;
    }

    public int getActionbarHeight() {
        return actionbarHeight;
    }

    public void setActionbarHeight(int actionbarHeight) {
        this.actionbarHeight = actionbarHeight;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public Point getmRootLayoutOrigin() {
        return mRootLayoutOrigin;
    }

    public void setmRootLayoutOrigin(int[] coordinates){
        if(coordinates.length < 2){
            return;
        }
        this.mRootLayoutOrigin.set(coordinates[0], coordinates[1]);
    }
}
