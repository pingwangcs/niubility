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

    private static final String PARAM_OFFSET = "&offset=";
    private static final String PARAM_BATCH = "&batch_size=";

    private String url;
    private ResultsListener mPhotoWallListener;

    private static PhotoWallClient mInstance = null;

    private PhotoWallClient() {
    }

    public static PhotoWallClient getInstance(String url) {
        if(mInstance == null || url != null ) {
            mInstance = new PhotoWallClient(url);
        }
        return mInstance;
    }

    private PhotoWallClient(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public JsonObjectRequest getFetchRequest() {
        return getLoadMoreRequest(0);
    }

    @Override
    public JsonObjectRequest getLoadMoreRequest(int offset) {
        final ArrayList<PhotoItem> results = new ArrayList<PhotoItem>();
        Log.w("wztw", "more request:"+url+PARAM_BATCH+Integer.MAX_VALUE);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url+PARAM_OFFSET+offset, null,
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url+PARAM_BATCH+Integer.MAX_VALUE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        try {
                            JSONObject result = response.getJSONObject("result");
                            JSONArray resultsEntitiesJsonArray = result.getJSONArray("entities");

                            boolean hasMore = result.getString("has_more").equals("true");
                            if(!hasMore){
                                mPhotoWallListener.whenNoMoreResults();
                            }


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

}
