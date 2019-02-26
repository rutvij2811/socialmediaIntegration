package com.example.tsfloginintegration;

import android.os.AsyncTask;

import java.net.MalformedURLException;
import java.net.URL;

public class fetchLnData extends AsyncTask<Void,Void,Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://www.linkedin.com/oauth/v2/authorization/812tg2kbp5x1gp");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
