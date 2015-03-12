package com.zhaohu.niubility.client.clients;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zhaohu.niubility.client.listeners.ResultsListener;
import com.zhaohu.niubility.results.items.EventItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wen on 2/22/15.
 */
public class HomeResultsClient implements ResultsClient {
    private final static String RESULT_URL = "http://51zhaohu.com/services/api/rest/json/?method=event.search&keyword=All&offset=";
    private final static int COUNTS_PER_PAGE = 20;
    private ResultsListener mHomeResultsListener;

    private static HomeResultsClient mInstance = null;

    public static HomeResultsClient getInstance() {
        if(mInstance == null) {
            mInstance = new HomeResultsClient();
        }
        return mInstance;
    }

    @Override
    public String getUrl() {
        return RESULT_URL;
    }

    @Override
    public JsonObjectRequest getFetchRequest() {
        return getLoadMoreRequest(0);
    }

    public JsonObjectRequest getLoadMoreRequest(int offset) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(RESULT_URL+offset, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        ArrayList<EventItem> results = new ArrayList<EventItem>();
                        try {
                            JSONObject result = response.getJSONObject("result");

                            boolean hasMore = result.getString("has_more").equals("true");
                            if(!hasMore){
                                mHomeResultsListener.whenNoMoreResults();
                            }

                            JSONArray resultsEntitiesJsonArray = result.getJSONArray("entities");
                            for (int i=0; i<resultsEntitiesJsonArray.length(); i++) {
                                JSONObject object = (JSONObject) resultsEntitiesJsonArray.get(i);
                                String title = object.getString("title");
//                                Log.d("TAG", title);

                                EventItem eventItem = new EventItem(object);
                                results.add(eventItem);
                            }

                            mHomeResultsListener.update(results);

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
        mHomeResultsListener = listener;
    }
}