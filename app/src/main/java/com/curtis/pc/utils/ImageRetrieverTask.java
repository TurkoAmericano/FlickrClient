package com.curtis.pc.utils;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageRetrieverTask  extends AsyncTask<Void, Void, String> {

    private Exception exception;

    @Override
    protected String doInBackground(Void... params) {
        try {

            URL url = new URL("https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=dfbeee85ac373f24739fe537f5d7f4ac&extras=url_m&format=json&nojsoncallback=1&auth_token=72157679053780041-107dac74a9286aa4&api_sig=0b1c6b6f1ccfccbdae0054190539a4e7");
            HttpURLConnection  urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            String result = stringBuilder.toString();

            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }

    protected void onPreExecute() {
        //progressBar.setVisibility(View.VISIBLE);
        //responseView.setText("");
    }

}
