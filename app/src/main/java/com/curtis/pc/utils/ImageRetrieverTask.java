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

            URL url = new URL("https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=8213afbf41516da43fd1dfa4c36954b0&extras=url_s&per_page=20&page=1&format=json&nojsoncallback=1&auth_token=72157680803935026-64774a3dbbf9792b&api_sig=f4addf00b7865aff88de5c840b8663be");
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
