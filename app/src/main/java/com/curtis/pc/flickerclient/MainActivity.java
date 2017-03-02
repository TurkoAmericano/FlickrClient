package com.curtis.pc.flickerclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
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

            ScaleImageView imageView = new ScaleImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 250));

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
                imageView = (ScaleImageView)convertView;
            }

            return imageView;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ImageView(context);

    }
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ScaleImageView bmImage;

    public DownloadImageTask(ScaleImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        int startX = 0;
        int startY = 0;

        if (bitmap.getWidth() > 250) {
            startX = bitmap.getWidth() - 250;
        }

        if (bitmap.getHeight() > 250) {
            startY = bitmap.getHeight() - 250;
        }

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, startX, startY, 250, 250);

        return resizedBitmap;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

class ScaleImageView extends ImageView {

    public ScaleImageView(Context context) {
        super(context);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            Drawable drawable = getDrawable();
            if (drawable == null) {
                setMeasuredDimension(0, 0);
            } else {
                int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
                int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
                if (measuredHeight == 0 && measuredWidth == 0) { //Height and width set to wrap_content
                    setMeasuredDimension(measuredWidth, measuredHeight);
                } else if (measuredHeight == 0) { //Height set to wrap_content
                    int width = measuredWidth;
                    int height = width * drawable.getIntrinsicHeight() / drawable.getIntrinsicWidth();
                    setMeasuredDimension(width, height);
                } else if (measuredWidth == 0) { //Width set to wrap_content
                    int height = measuredHeight;
                    int width = height * drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight();
                    setMeasuredDimension(width, height);
                } else { //Width and height are explicitly set (either to match_parent or to exact value)
                    setMeasuredDimension(measuredWidth, measuredHeight);
                }
            }
        } catch (Exception e) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}

