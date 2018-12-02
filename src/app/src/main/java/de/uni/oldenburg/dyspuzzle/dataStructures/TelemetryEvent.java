package de.uni.oldenburg.dyspuzzle.dataStructures;


import android.graphics.Point;
//import android.text.format.Time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class TelemetryEvent {

    public TelemetryEvent(EventType eventType){
        this.date = new Date();
        this.eventType = eventType;
    };

    private EventType eventType;
    private Date date;
    private long duration = 0;
    private String info = null;
    private Point point = null;
    private double angle = 0;
    private boolean snappedIn = false;
    private boolean setCorrectly = false;
    private int difficultyDegree;
    private int viewId;


    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Date getDateTime() {
        return date;
    }

    public void setDateTime(Date date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public boolean isSnappedIn() {
        return snappedIn;
    }

    public boolean isSetCorrectly() {
        return setCorrectly;
    }

    public void setSetCorrectly(boolean setCorrectly) {
        this.setCorrectly = setCorrectly;
    }

    public void setSnappedIn(boolean snappedIn) {
        this.snappedIn = snappedIn;
    }

    public int getDifficultyDegree() {
        return difficultyDegree;
    }

    public void setDifficultyDegree(int difficultyDegree) {
        this.difficultyDegree = difficultyDegree;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    private String getIsoTime(Date date){
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss,SSS'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }
}
