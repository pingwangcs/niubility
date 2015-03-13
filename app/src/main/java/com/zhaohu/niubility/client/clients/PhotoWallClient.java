package com.zhaohu.niubility.client.clients;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhaohu.niubility.client.listeners.ResultsListener;
import com.zhaohu.niubility.results.items.PhotoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wen on 2/22/15.
 */
public class PhotoWallClient implements ResultsClient {
    private String url;
    private ResultsListener mPhotoWallListener;

    private static PhotoWallClient mInstance = null;

    private PhotoWallClient() {

    }

    public static PhotoWallClient getInstance() {
        if(mInstance == null) {
            mInstance = new PhotoWallClient();
        }
        return mInstance;
    }


    public PhotoWallClient(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public JsonObjectRequest getFetchRequest() {
        final ArrayList<PhotoItem> results = new ArrayList<PhotoItem>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        try {
                            JSONObject result = response.getJSONObject("result");
                            JSONArray resultsEntitiesJsonArray = result.getJSONArray("entities");
                            for (int i=0; i<resultsEntitiesJsonArray.length(); i++) {
                                JSONObject object = (JSONObject) resultsEntitiesJsonArray.get(i);
                                String title = object.getString("title");
                                Log.d("TAG", title);

                                PhotoItem photoItem = new PhotoItem(object);
                                results.add(photoItem);
                            }

                            mPhotoWallListener.update(results);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error) ;
            }
        });
        return jsonObjectRequest;
    }

    @Override
    public void addListener(ResultsListener listener) {
        mPhotoWallListener = listener;
    }

    @Override
    public JsonObjectRequest getLoadMoreRequest(int offset) {
        return null;
    }

}
