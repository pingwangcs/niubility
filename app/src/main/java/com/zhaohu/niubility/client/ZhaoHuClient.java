package com.zhaohu.niubility.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.EventLogTags;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zhaohu.niubility.R;
import com.zhaohu.niubility.results.EventItem;
import com.zhaohu.niubility.results.HotEventListItem;
import com.zhaohu.niubility.results.HotEventResultsListAdapter;
import com.zhaohu.niubility.results.PhotoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wen on 1/13/15.
 */
public class ZhaoHuClient {

    private static ZhaoHuClient mInstance = null;

    private Context mContext;
    private RequestQueue mQueue;
    private Set<HomeResultsListener> mHomeResultsListeners;
    private Set<HotResultsListener> mHotResultsListeners;
    private Set<PhotoWallListener> mPhotoWallListeners;

    private final static String HOME_URL = "http://51zhaohu.com/services/api/rest/json/?method=event.search&keyword=All&offset=0";
    private final static String HOT_URL = "http://51zhaohu.com/services/api/rest/json/?method=event.search&featured=y&offset=0";
    private final static String PHOTO_WALL_URL = "http://51zhaohu.com/services/api/rest/json/?method=album.list&offset=0";

    public ZhaoHuClient(Context context) {
        mContext = context;
        mQueue = Volley.newRequestQueue(mContext);
        mHomeResultsListeners = new HashSet<HomeResultsListener>();
        mHotResultsListeners = new HashSet<HotResultsListener>();
        mPhotoWallListeners = new HashSet<PhotoWallListener>();
    }

    public static ZhaoHuClient getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new ZhaoHuClient(context);
        }
        return mInstance;
    }

    public void fetchHomeResults() {

        final ArrayList<EventItem> results = new ArrayList<EventItem>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(HOME_URL, null,
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
//                                Log.d("TAG", title);

                                EventItem eventItem = new EventItem(object);
                                results.add(eventItem);
                            }

                            for(HomeResultsListener listener: mHomeResultsListeners) {
                                listener.update(results);
                            }

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

        mQueue.add(jsonObjectRequest);
    }

    public void fetchHotResults() {

        final ArrayList<HotEventListItem> results = new ArrayList<HotEventListItem>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(HOT_URL, null,
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
//                                Log.d("TAG", title);

                                HotEventListItem eventItem = new HotEventListItem(object);
                                results.add(eventItem);
                            }

                            for(HotResultsListener listener: mHotResultsListeners) {
                                listener.update(results);
                            }

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

        mQueue.add(jsonObjectRequest);
    }

    public void fetchPhotoWall() {

        final ArrayList<PhotoItem> results = new ArrayList<PhotoItem>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(PHOTO_WALL_URL, null,
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

                            for(PhotoWallListener listener: mPhotoWallListeners) {
                                listener.update(results);
                            }

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

        mQueue.add(jsonObjectRequest);
    }
    
    public void addHomeResultsListener(HomeResultsListener listener) {
        mHomeResultsListeners.add(listener);
    }

    public void removeHomeResultsListener(HomeResultsListener listener) {
        mHomeResultsListeners.remove(listener);
    }

    public void addHotResultsListener(HotResultsListener listener) {
        mHotResultsListeners.add(listener);
    }

    public void removeHotResultsListener(HotResultsListener listener) {
        mHotResultsListeners.remove(listener);
    }

    public void addPhotoWallListener(PhotoWallListener listener) {
        mPhotoWallListeners.add(listener);
    }

    public void loadImage(String imageUrl, ImageView imageView) {

        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.drawable.loading, R.drawable.pic_load_failed);

        imageLoader.get(imageUrl, listener);
//        imageLoader.get("4444", listener);
    }
}
