package com.zhaohu.niubility.client;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.zhaohu.niubility.R;
import com.zhaohu.niubility.results.AlbumItem;
import com.zhaohu.niubility.results.EventItem;
import com.zhaohu.niubility.results.PhotoItem;
import com.zhaohu.niubility.views.ZoomImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
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
    private Set<AlbumListener> mAlbumListeners;

    private Set<PhotoWallListener> mPhotoWallListeners;

    private final static String HOME_URL = "http://51zhaohu.com/services/api/rest/json/?method=event.search&keyword=All&offset=0";
    private final static String HOT_URL = "http://51zhaohu.com/services/api/rest/json/?method=event.search&featured=y&offset=0";
    private final static String ALBUM_URL = "http://51zhaohu.com/services/api/rest/json/?method=album.list&offset=0";

    public ZhaoHuClient(Context context) {
        mContext = context;
        mQueue = Volley.newRequestQueue(mContext);
        mHomeResultsListeners = new HashSet<HomeResultsListener>();
        mHotResultsListeners = new HashSet<HotResultsListener>();
        mAlbumListeners = new HashSet<AlbumListener>();

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

        final ArrayList<EventItem> results = new ArrayList<EventItem>();

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

                                EventItem eventItem = new EventItem(object);
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

    public void fetchAlbums() {

        final ArrayList<AlbumItem> results = new ArrayList<AlbumItem>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ALBUM_URL, null,
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

                            for(AlbumListener listener: mAlbumListeners) {
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

    public void fetchPhotoWall(String url) {

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
                Log.e("TAG", error.getMessage(), error) ;
            }
        });

        mQueue.add(jsonObjectRequest);
    }
    
    public void addHomeResultsListener(HomeResultsListener listener) {
        mHomeResultsListeners.add(listener);
    }

    public void addHotResultsListener(HotResultsListener listener) {
        mHotResultsListeners.add(listener);
    }

    public void addPhotoWallListener(PhotoWallListener listener) {
        mPhotoWallListeners.add(listener);
    }

    public void addAlbumListener(AlbumListener listener) {
        mAlbumListeners.add(listener);
    }

    public void setNetworkImage(String imageUrl, NetworkImageView imageView) {
        ImageLoader imageLoader = new ImageLoader(mQueue, BitmapCache.getInstance(mContext));

        imageView.setDefaultImageResId(R.drawable.loading);
        imageView.setErrorImageResId(R.drawable.pic_load_failed);
        imageView.setImageUrl(imageUrl, imageLoader);
    }

    public void loadImage(String imageUrl, ImageView imageView) {

        ImageLoader imageLoader = new ImageLoader(mQueue, BitmapCache.getInstance(mContext));

        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.drawable.loading, R.drawable.pic_load_failed);

        imageLoader.get(imageUrl, listener);
    }

    public void loadImageUsingRequest(String url, final ZoomImageView zoomImageView) {
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        zoomImageView.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pic_load_failed);
                zoomImageView.setImageBitmap(bitmap);
            }
        });
        mQueue.add(imageRequest);
    }

    public void loadImage(String url, final ZoomImageView zoomImageView) {
        ImageLoader imageLoader = new ImageLoader(mQueue, BitmapCache.getInstance(mContext));

        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                zoomImageView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pic_load_failed);
                zoomImageView.setImageBitmap(bitmap);
            }
        };

        imageLoader.get(url, listener);
    }
}
