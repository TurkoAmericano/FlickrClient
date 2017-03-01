package com.curtis.pc.utils;

import com.curtis.pc.models.Image;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;

import java.util.ArrayList;
import java.util.List;


public class ImageRetriever {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public List<Image> GetLatestImages()
    {
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=8213afbf41516da43fd1dfa4c36954b0&extras=url_s&per_page=20&page=1&format=json&nojsoncallback=1&auth_token=72157680803935026-64774a3dbbf9792b&api_sig=f4addf00b7865aff88de5c840b8663be";

        RequestHandle requestHandle = client.get(url, null, null);

        return new ArrayList<>();

    }


}
