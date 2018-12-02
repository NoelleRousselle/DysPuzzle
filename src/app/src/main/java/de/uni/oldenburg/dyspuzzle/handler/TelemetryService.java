package de.uni.oldenburg.dyspuzzle.handler;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.uni.oldenburg.dyspuzzle.BuildConfig;
import de.uni.oldenburg.dyspuzzle.dataStructures.Data;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// service for sending data
public class TelemetryService {

    private static TelemetryService mTelemetryService = null;
    private static final String mBaseUrl = "http://srvgvm33.offis.uni-oldenburg.de:8080/1/";
    private static ITelemetryService mRestService;
    private static ArrayList<String> mLogMessages = new ArrayList<>();

    // a private constructor so no instances can be made outside this class
    private TelemetryService() {}

    // Everytime an instance is needed, call this function
    // synchronized to make the call thread-safe
    public static synchronized TelemetryService getInstance() {

        if(mTelemetryService == null) {
            mTelemetryService = new TelemetryService();
            mRestService = createRestService();
        }

        return mTelemetryService;
    }

    public void resetLogMessages(){
        mLogMessages = new ArrayList<>();
    }

    public ArrayList<String> getLogMessages(){
        return mLogMessages;
    }

    public Data getTelemetryDataFromFile(Context context, String fileName) throws IOException {

        String json;
        Gson gson = new Gson();

        // open file and read all data
        FileInputStream fileInputStream  = null;
        try {
            fileInputStream = context.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuffer stringBuffer = new StringBuffer();

        while((json=bufferedReader.readLine())!=null){
            stringBuffer.append(json);
        }

        Data data = gson.fromJson(String.valueOf(stringBuffer), Data.class);
        return data;
    }

    public void sendTelemetryData(String fileName, Data data, IServiceCallBack callBack){

        // send data to server
        sendDataToServer(data, new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    callBack.onSuccess(fileName);
                }
                else{
                    callBack.onFailure(fileName);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                addLogMessage(t.getMessage());
                callBack.onFailure(fileName);
            }

        });
    }

    private void sendDataToServer(Data data, Callback<Object> callback){
        try {
            Call<Object> response = mRestService.sendTelemetryData(data);
            response.enqueue(callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static OkHttpClient.Builder createLoggingInterceptor(){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {

            // https://futurestud.io/tutorials/retrofit-2-log-requests-and-responses
            // Add interceptor for logging the response time
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override public void log(String message) {
                    addLogMessage(message);
                }
            });
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

            httpClient.addInterceptor(logging);
        }

        return httpClient;
    }

    private static ITelemetryService createRestService(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient.Builder httpClient = createLoggingInterceptor();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        ITelemetryService service = retrofit.create(ITelemetryService.class);
        return service;
    }

    private static void addLogMessage(String message){

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss:SSS");
        String nowString = formatter.format(now);

        mLogMessages.add(nowString + ": " + message);
    }
}
