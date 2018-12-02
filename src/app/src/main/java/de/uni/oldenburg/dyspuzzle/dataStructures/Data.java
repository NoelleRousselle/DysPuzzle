package de.uni.oldenburg.dyspuzzle.dataStructures;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import de.uni.oldenburg.dyspuzzle.handler.TelemetryHandler;


public class Data {

    List telemetryEvents = TelemetryHandler.getInstance().getTelemetryEvents();
    Person person = Person.getInstance();
    Device device = Device.getInstance();

    public String toJson(){
        Data data = new Data();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss,SSS'Z'").create();
        String json = gson.toJson(data);
        return json;
    }

}


