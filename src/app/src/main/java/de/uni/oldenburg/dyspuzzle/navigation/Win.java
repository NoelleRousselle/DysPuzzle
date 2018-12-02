package de.uni.oldenburg.dyspuzzle.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.uni.oldenburg.dyspuzzle.R;
import de.uni.oldenburg.dyspuzzle.dataStructures.Data;
import de.uni.oldenburg.dyspuzzle.dataStructures.EventType;
import de.uni.oldenburg.dyspuzzle.dataStructures.TelemetryEvent;
import de.uni.oldenburg.dyspuzzle.handler.IServiceCallBack;
import de.uni.oldenburg.dyspuzzle.handler.TelemetryHandler;
import de.uni.oldenburg.dyspuzzle.handler.TelemetryService;

public class Win extends AppCompatActivity {
    private Timer timer = new Timer();
    private TelemetryHandler telemetryHandler = TelemetryHandler.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        saveGameEndEvent();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        // get actionbar layout file
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);

//        Data data = new Data();
//        String json = data.toJson();
        sendData();
    }

    public String saveDataToFile(Data data){
        try {
            Gson gson = new Gson();

            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String nowString = formatter.format(now);
            String filename = nowString + "_telemetryData";

            // New telemetryData file
            FileOutputStream fileOutputStream = openFileOutput(filename, MODE_PRIVATE);

            // Serialize telemetryData instance to a json object
            String json = gson.toJson(data);

            // Write the json object in the file
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();

            return filename;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void successSendData(Toast toast, String filename){

        toast.cancel();
        deleteFile(filename);
        telemetryHandler.clearAllEvents();

        callActivityTimer();

    }

    private void errorSendData(Context context, Toast toast){

        toast.cancel();
        toast.makeText(context, "Daten konnten nicht gesendet werden!", Toast.LENGTH_LONG).show();

        // Check if there are only 10 files
        String[] fileNames = fileList();
        if(fileNames.length == 12) {
            Arrays.sort(fileNames);
            deleteFile(fileNames[0]);
        }

        telemetryHandler.clearAllEvents();

        callActivityTimer();
    }

    private void sendData(){

        Context context = this;
        final Toast toast = Toast.makeText(this, "Daten werden gesendet!", Toast.LENGTH_LONG);
        toast.show();

        // Create the telemetry data
        Data data = new Data();

        // save data local in a file
        String fileName = saveDataToFile(data);

        TelemetryService telemetryService = TelemetryService.getInstance();
        telemetryService.sendTelemetryData(fileName, data, new IServiceCallBack() {
            @Override
            public void onSuccess(String response) {
                toast.cancel();
                successSendData(toast, fileName);
            }

            @Override
            public void onFailure(String response) {
                errorSendData(context, toast);
            }
        });
    }

    private void saveGameEndEvent() {
        TelemetryEvent telemetryEvent;
        telemetryEvent = new TelemetryEvent(EventType.ENDGAME);
        telemetryHandler.addEvent(telemetryEvent);

    }

    private synchronized void callActivityTimer(){

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Win.this, StartScreen.class);
                startActivity(intent);
                finish();
            }
        },1000);
    }

}

