package de.uni.oldenburg.dyspuzzle.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import de.uni.oldenburg.dyspuzzle.R;
import de.uni.oldenburg.dyspuzzle.dataStructures.Data;
import de.uni.oldenburg.dyspuzzle.dataStructures.Device;
import de.uni.oldenburg.dyspuzzle.dataStructures.EventType;
import de.uni.oldenburg.dyspuzzle.dataStructures.Person;
import de.uni.oldenburg.dyspuzzle.dataStructures.PersonData;
import de.uni.oldenburg.dyspuzzle.dataStructures.TelemetryEvent;
import de.uni.oldenburg.dyspuzzle.handler.IServiceCallBack;
import de.uni.oldenburg.dyspuzzle.handler.TelemetryHandler;
import de.uni.oldenburg.dyspuzzle.handler.TelemetryService;
import de.uni.oldenburg.dyspuzzle.layoutGenerator.ContextInfo;

public class MainActivity extends AppCompatActivity {

    PersonData personData;
    Person person = Person.getInstance();

    private Timer timer = new Timer();

    TelemetryService mTelemetryService = TelemetryService.getInstance();

    TelemetryEvent telemetryEvent;
    TelemetryHandler telemetryHandler = TelemetryHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        // get actionbar layout file
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);

        saveStartAppEvent();

        sendTelemetryData();
        readPersonDataFile();
        callActivityTimer();
        safeContextInfo();

    }


    private void safeContextInfo() {
        ContextInfo contextInfo = new ContextInfo(this);
        Device device = Device.getInstance();

        device.setScreenWidth(contextInfo.getTotalScreenWidth());
        device.setScreenHeight(contextInfo.getTotalScreenHeight());
        device.setNavigationBarHeight(contextInfo.getNavigationBarHeight());
        device.setActionbarHeight(contextInfo.getActionBarHeight());
        device.setStatusbarHeight(contextInfo.getStatusBarHeight());
    }

    private void saveStartAppEvent() {

        telemetryEvent = new TelemetryEvent(EventType.STARTAPP);
        telemetryHandler.addEvent(telemetryEvent);

    }

    public synchronized void callActivityTimer(){

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

//                TelemetryService telemetryService = TelemetryService.getInstance();
//                ArrayList<String> messages = telemetryService.getLogMessages();

                // Check if there are already infos of the person
                // if there are no infos go to EnterData activity
                // else go to StartScreen activity
                if(person.getAge() == 0
                        || person.getGender() == null
                        || person.getPreferredHand() == null)
                {
                    String uniqueId = UUID.randomUUID().toString();
                    person.setUniqueId(uniqueId);

                    Intent intent = new Intent(MainActivity.this, PrivacyStatement.class);
                    startActivity(intent);
                    finish();

                } else{
                    Intent intent = new Intent(MainActivity.this, StartScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);

    }

    private void sendTelemetryData() {

        String fileDir = getFilesDir().getPath() + "/";
        String[] fileNames = fileList();
        String allFiles = "";

        for(int i = 0; i < 1; i++) {
            for(String fileName : fileNames){

                String filePath = fileDir + fileName;
                allFiles = allFiles + fileName + "(" + fileSizeInKb(filePath) + "), ";
                if(!fileName.equalsIgnoreCase("personData")){
                    Data data = getTelemetryDataFromFile(fileName);
                    if(data == null) {
                        // TODO: Aus der Datei konnte kein Data Objekt erzeugt werden
                    } else {

                        mTelemetryService.sendTelemetryData(fileName, data, new IServiceCallBack() {

                            @Override
                            public void onSuccess(String fileName) {
                                handleServiceSuccess(fileName);
                            }

                            @Override
                            public void onFailure(String fileName) {
                                handleServiceFailure(fileName);
                            }
                        });
                    }
                }
            }
        }
    }

    private void handleServiceSuccess(String fileName) {
        deleteFile(fileName);
    }

    private void handleServiceFailure(String fileName) {

    }

    private Data getTelemetryDataFromFile(String fileName) {

        try {
            return mTelemetryService.getTelemetryDataFromFile(this, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private long fileSizeInKb(String filePath) {

        File file = new File(filePath);
        long fileSize = file.length();
        return fileSize / 1024;
    }

    private void readPersonDataFile(){
        try {
            String json;
            Gson gson = new Gson();

            // Read the file personData
            FileInputStream fileInputStream  = openFileInput("personData");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();

            while((json=bufferedReader.readLine())!=null){

                // Safe the content of this file in a String
                stringBuffer.append(json);

                // Deserialize the json String to a personData object
                personData = gson.fromJson(json,PersonData.class);

                // Safe the information from personData in the person instance
                personDataToPerson();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void personDataToPerson(){
        person.setAge(personData.getAge());
        person.setGender(personData.getGender());
        person.setPreferredHand(personData.getPreferredHand());
        person.setDislexia(personData.getDislexia());
        person.setUniqueId(personData.getUniqueId());
    }

}
