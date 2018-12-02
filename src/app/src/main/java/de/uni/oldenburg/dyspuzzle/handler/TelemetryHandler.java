package de.uni.oldenburg.dyspuzzle.handler;

import android.graphics.Point;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.uni.oldenburg.dyspuzzle.dataStructures.TelemetryEvent;

// singleton class which handles all events
public class TelemetryHandler {

    // List which contains all fired events
    public List telemetryEvents = new ArrayList();

    private Point rootLayoutOrigin = new Point();

    private static TelemetryHandler telemetryHandler = null;

    // a private constructor so no instances can be made outside this class
    private TelemetryHandler() {}

    // Everytime an instance is needed, call this function
    // synchronized to make the call thread-safe
    public static synchronized TelemetryHandler getInstance() {

        if(telemetryHandler == null)
            telemetryHandler = new TelemetryHandler();

        return telemetryHandler;
    }

    public void addEvent(TelemetryEvent telemetryEvent){

        // if the event contains coordinates the origin of the root layout is considered
        if(telemetryEvent.getPoint() != null){
            Point newPoint = telemetryEvent.getPoint();
            newPoint.set(newPoint.x - rootLayoutOrigin.x, newPoint.y - rootLayoutOrigin.y);
            telemetryEvent.setPoint(newPoint);
        }
        telemetryEvents.add(telemetryEvent);

    }

    public List getTelemetryEvents() {

        Date startTime = ((TelemetryEvent) telemetryEvents.get(0)).getDateTime();

        for (Object event: telemetryEvents) {
            TelemetryEvent telemetryEvent = (TelemetryEvent) event;
            long diffInMillies = Math.abs(startTime.getTime() - telemetryEvent.getDateTime().getTime());
            //Date date = new Date(diffInMillies);
            telemetryEvent.setDuration(diffInMillies);
        }

        return telemetryEvents;
    }

    public void clearAllEvents(){
        telemetryEvents.clear();
    }

    public String toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(telemetryEvents);
        return json;
    }

    public void setRootLayoutOrigin(int[] coordinates){
        if(coordinates.length < 2){
            return;
        }
        this.rootLayoutOrigin.set(coordinates[0], coordinates[1]);
    }

}
