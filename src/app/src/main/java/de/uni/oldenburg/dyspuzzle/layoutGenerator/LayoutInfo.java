package de.uni.oldenburg.dyspuzzle.layoutGenerator;

import android.content.res.Configuration;

import de.uni.oldenburg.dyspuzzle.layoutGenerator.LayoutDetail;

public class LayoutInfo {

    private LayoutDetail[] details;
    private int orientation;

    public LayoutInfo() {
        orientation = Configuration.ORIENTATION_LANDSCAPE;
    }

    public LayoutDetail[] getDetails() {
        return details;
    }

    public void setDetails(LayoutDetail[] details) {
        this.details = details;
    }

    public int getOrientation(){
        return this.orientation;
    }

    public void setOrientation(int orientation){
        this.orientation = orientation;
    }
}
