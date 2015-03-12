package com.zhaohu.niubility.client.clients;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhaohu.niubility.client.listeners.ResultsListener;
import com.zhaohu.niubility.results.items.AlbumItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wen on 2/22/15.
 */
public class AlbumClient implements ResultsClient {
    private final static String RESULT_URL = "http://51zhaohu.com/services/api/rest/json/?method=album.list&offset=0";
    private ResultsListener mAlbumListener;

    @Override
    public String getUrl() {
        return RESULT_URL;
    }

    @Override
    public JsonObjectRequest getFetchRequest() {
        final ArrayList<AlbumItem> results = new ArrayList<AlbumItem>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(RESULT_URL, null,
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

                                AlbumItem albumItem = new AlbumItem(object);
                                results.add(albumItem);
                            }

                            mAlbumListener.update(results);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        return jsonObjectRequest;
    }

    @Override
    public void addListener(ResultsListener listener) {
        mAlbumListener = listener;
    }

    @Override
    public JsonObjectRequest getLoadMoreRequest(int offset) {
        return null;
    }

}