package com.curtis.pc.flickerclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.curtis.pc.utils.ImageRetrieverTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private GridView PhotoGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageRetrieverTask task = new ImageRetrieverTask();

        String result = null;
        try {
            result = task.execute().get();

            JSONObject object = new JSONObject(result).getJSONObject("photos");
            JSONArray images = object.getJSONArray("photo");

            GridView gridview = (GridView) findViewById(R.id.PhotoGrid);

            gridview.setAdapter(new ImageAdapter(this, images));

            String x = "";

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}

class ImageAdapter extends BaseAdapter
{
    private Context context;
    private JSONArray jsonArray;

    public ImageAdapter(Context c, JSONArray j)
    {
        context = c;
        jsonArray = j;
    }

    //---returns the number of images---
    public int getCount() {
        return 20;
    }

    //---returns the ID of an item---
    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    //---returns an ImageView view---
    public View getView(int position, View convertView, ViewGroup parent)
    {



        try {

            ImageView imageView = new ImageView(context);

            if (convertView == null)
            {
                JSONObject object = jsonArray.getJSONObject(position);
                String url = object.getString("url_m");
                String title = object.getString("title");

                new DownloadImageTask(imageView)
                        .execute(url);

            }
            else
            {
                imageView = (ImageView)convertView;
            }

            return imageView;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ImageView(context);

    }
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

