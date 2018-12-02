package de.uni.oldenburg.dyspuzzle.handler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

// https://hackjutsu.com/2015/11/30/Test%20Internet%20Availability%20in%20Android/
// https://stackoverflow.com/questions/6343166/how-do-i-fix-android-os-networkonmainthreadexception
public class ConnectionDetector extends AsyncTask<String, Void, Boolean> {

    private Exception exception;

    private Context _context;
    public ConnectionDetector(Context context) {
        this._context = context;
    }

    protected Boolean doInBackground(String... urls) {
        try {

            if (networkConnectivity()) {
                try {
                    HttpURLConnection urlc = (HttpURLConnection) (new URL(
                            "http://www.google.com").openConnection());
                    urlc.setRequestProperty("User-Agent", "Test");
                    urlc.setRequestProperty("Connection", "close");
                    urlc.setConnectTimeout(3000);
                    urlc.setReadTimeout(4000);
                    urlc.connect();
                    return (urlc.getResponseCode() == 200);
                } catch (IOException e) {
                    return (false);
                }
            } else {
                return false;
            }

        } catch (Exception e) {
            this.exception = e;
            return false;
        }
    }

    private boolean networkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}